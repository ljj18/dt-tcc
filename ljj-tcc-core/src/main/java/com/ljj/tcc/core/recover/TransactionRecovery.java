
package com.ljj.tcc.core.recover;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ljj.tcc.api.TransactionPhase;
import com.ljj.tcc.api.TransactionType;
import com.ljj.tcc.core.OptimisticLockException;
import com.ljj.tcc.core.Transaction;
import com.ljj.tcc.core.TransactionRepository;
import com.ljj.tcc.core.support.TransactionConfigurator;

/**
 * 事务恢复
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public class TransactionRecovery {

    static final Logger logger = LoggerFactory.getLogger(TransactionRecovery.class);

    private TransactionConfigurator transactionConfigurator;

    public TransactionRecovery(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }
    
    
    public void startRecover() {
        List<Transaction> transactions = loadErrorTransactions();
        recoverErrorTransactions(transactions);
    }

    private List<Transaction> loadErrorTransactions() {

        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        TransactionRepository transactionRepository = transactionConfigurator.getTransactionRepository();
        RecoverConfig recoverConfig = transactionConfigurator.getRecoverConfig();
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(currentTimeInMillis - recoverConfig.getRecoverDuration() * 1000, 0, ZoneOffset.of("+8")); 
        return transactionRepository.findAllUnmodifiedSince(ldt);
    }

    private void recoverErrorTransactions(List<Transaction> transactions) {

        for (Transaction transaction : transactions) {

            if (transaction.getRetriedCount() > transactionConfigurator.getRecoverConfig().getMaxRetryCount()) {

                logger.error(String.format(
                    "recover failed with max retry count,will not try again. txid:%s, status:%s,retried count:%d,transaction content:%s",
                    transaction.getXid(), transaction.getPhase().getId(), transaction.getRetriedCount(),
                    JSONObject.toJSONString(transaction)));
                continue;
            }

            if (transaction.getTransactionType().equals(TransactionType.BRANCH)
                && (transaction.getGmtCreate().toInstant(ZoneOffset.of("+8")).toEpochMilli()
                    + transactionConfigurator.getRecoverConfig().getMaxRetryCount()
                        * transactionConfigurator.getRecoverConfig().getRecoverDuration() * 1000 > System
                            .currentTimeMillis())) {
                continue;
            }

            try {
                transaction.addRetriedCount();

                if (transaction.getPhase().equals(TransactionPhase.CONFIRMING)) {

                    transaction.changePhase(TransactionPhase.CONFIRMING);
                    transactionConfigurator.getTransactionRepository().update(transaction);
                    transaction.commit();
                    transactionConfigurator.getTransactionRepository().delete(transaction);

                } else if (transaction.getPhase().equals(TransactionPhase.CANCELLING)
                    || transaction.getTransactionType().equals(TransactionType.ROOT)) {

                    transaction.changePhase(TransactionPhase.CANCELLING);
                    transactionConfigurator.getTransactionRepository().update(transaction);
                    transaction.rollback();
                    transactionConfigurator.getTransactionRepository().delete(transaction);
                }

            } catch (Throwable throwable) {

                if (throwable instanceof OptimisticLockException
                    || ExceptionUtils.getRootCause(throwable) instanceof OptimisticLockException) {
                    logger.warn(String.format(
                        "optimisticLockException happened while recover. txid:%s, status:%s,retried count:%d,transaction content:%s",
                        transaction.getXid(), transaction.getPhase().getId(), transaction.getRetriedCount(),
                        JSONObject.toJSONString(transaction)), throwable);
                } else {
                    logger.error(
                        String.format("recover failed, txid:%s, status:%s,retried count:%d,transaction content:%s",
                            transaction.getXid(), transaction.getPhase().getId(), transaction.getRetriedCount(),
                            JSONObject.toJSONString(transaction)),
                        throwable);
                }
            }
        }
    }

    public void setTransactionConfigurator(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }
}
