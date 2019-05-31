package com.ljj.tcc.core.support;

import com.ljj.tcc.core.TransactionManager;
import com.ljj.tcc.core.TransactionRepository;
import com.ljj.tcc.core.recover.RecoverConfig;

/**
 * Created by liangjinjing on 2/24/17.
 */
public interface TransactionConfigurator {

    TransactionManager getTransactionManager();

    TransactionRepository getTransactionRepository();

    RecoverConfig getRecoverConfig();
}
