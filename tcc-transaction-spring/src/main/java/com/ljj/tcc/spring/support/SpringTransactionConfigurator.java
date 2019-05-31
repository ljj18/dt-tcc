package com.ljj.tcc.spring.support;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ljj.tcc.core.TransactionManager;
import com.ljj.tcc.core.TransactionRepository;
import com.ljj.tcc.core.recover.RecoverConfig;
import com.ljj.tcc.core.recover.impl.DefaultRecoverConfig;
import com.ljj.tcc.core.repository.CachableTransactionRepository;
import com.ljj.tcc.core.support.TransactionConfigurator;

/**
 * Created by liangjinjing on 11/11/15.
 */
@Component("transactionConfigurator")
public class SpringTransactionConfigurator implements TransactionConfigurator {

    private static volatile ExecutorService executorService = null;

    private RecoverConfig recoverConfig;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    private TransactionManager transactionManager;

    @PostConstruct
    public void init() {
        recoverConfig = DefaultRecoverConfig.INSTANCE;
        transactionManager = new TransactionManager();
        transactionManager.setTransactionRepository(transactionRepository);
        if (executorService == null) {
            synchronized (SpringTransactionConfigurator.class) {

                if (executorService == null) {
                    executorService =  new ThreadPoolExecutor(5, 50,
                        60L, TimeUnit.SECONDS,
                        new SynchronousQueue<Runnable>());
                }
            }
        }
        transactionManager.setExecutorService(executorService);
        if (transactionRepository instanceof CachableTransactionRepository) {
            ((CachableTransactionRepository) transactionRepository).setExpireDuration(recoverConfig.getRecoverDuration());
        }
    }

    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    @Override
    public RecoverConfig getRecoverConfig() {
        return recoverConfig;
    }
}
