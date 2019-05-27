
package com.ljj.tcc.core.recover;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ljj.tcc.api.TransactionStatus;
import com.ljj.tcc.core.OptimisticLockException;
import com.ljj.tcc.core.Transaction;
import com.ljj.tcc.core.TransactionRepository;
import com.ljj.tcc.core.common.TransactionType;
import com.ljj.tcc.core.support.TransactionConfigurator;

/**
 * Created by liangjinjing on 11/10/15.
 */
public class TransactionRecovery {

    static final Logger logger = LoggerFactory.getLogger(TransactionRecovery.class);

    private TransactionConfigurator transactionConfigurator;

    public void startRecover() {

        List<Transaction> transactions = loadErrorTransactions();

        recoverErrorTransactions(transactions);
    }

    private List<Transaction> loadErrorTransactions() {

        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();

        TransactionRepository transactionRepository = transactionConfigurator.getTransactionRepository();
        RecoverConfig recoverConfig = transactionConfigurator.getRecoverConfig();

        return transactionRepository
            .findAllUnmodifiedSince(new Date(currentTimeInMillis - recoverConfig.getRecoverDuration() * 1000));
    }

    private void recoverErrorTransactions(List<Transaction> transactions) {

        for (Transaction transaction : transactions) {

            if (transaction.getRetriedCount() > transactionConfigurator.getRecoverConfig().getMaxRetryCount()) {

                logger.error(String.format(
                    "recover failed with max retry count,will not try again. txid:%s, status:%s,retried count:%d,transaction content:%s",
                    transaction.getXid(), transaction.getStatus().getId(), transaction.getRetriedCount(),
                    JSONObject.toJSONString(transaction)));
                continue;
            }

            if (transaction.getTransactionType().equals(TransactionType.BRANCH)
                && (transaction.getCreateTime().getTime()
                    + transactionConfigurator.getRecoverConfig().getMaxRetryCount()
                        * transactionConfigurator.getRecoverConfig().getRecoverDuration() * 1000 > System
                            .currentTimeMillis())) {
                continue;
            }

            try {
                transaction.addRetriedCount();

                if (transaction.getStatus().equals(TransactionStatus.CONFIRMING)) {

                    transaction.changeStatus(TransactionStatus.CONFIRMING);
                    transactionConfigurator.getTransactionRepository().update(transaction);
                    transaction.commit();
                    transactionConfigurator.getTransactionRepository().delete(transaction);

                } else if (transaction.getStatus().equals(TransactionStatus.CANCELLING)
                    || transaction.getTransactionType().equals(TransactionType.ROOT)) {

                    transaction.changeStatus(TransactionStatus.CANCELLING);
                    transactionConfigurator.getTransactionRepository().update(transaction);
                    transaction.rollback();
                    transactionConfigurator.getTransactionRepository().delete(transaction);
                }

            } catch (Throwable throwable) {

                if (throwable instanceof OptimisticLockException
                    || ExceptionUtils.getRootCause(throwable) instanceof OptimisticLockException) {
                    logger.warn(String.format(
                        "optimisticLockException happened while recover. txid:%s, status:%s,retried count:%d,transaction content:%s",
                        transaction.getXid(), transaction.getStatus().getId(), transaction.getRetriedCount(),
                        JSONObject.toJSONString(transaction)), throwable);
                } else {
                    logger.error(
                        String.format("recover failed, txid:%s, status:%s,retried count:%d,transaction content:%s",
                            transaction.getXid(), transaction.getStatus().getId(), transaction.getRetriedCount(),
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
