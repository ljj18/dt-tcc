package com.ljj.tcc.spring;

import javax.annotation.PostConstruct;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.ljj.tcc.core.TransactionManager;
import com.ljj.tcc.core.interceptor.CompensableTransactionAspect;
import com.ljj.tcc.core.interceptor.CompensableTransactionInterceptor;
import com.ljj.tcc.core.support.TransactionConfigurator;

/**
 * Created by liangjinjing on 10/30/15.
 */
@Aspect
@Component
public class ConfigurableTransactionAspect extends CompensableTransactionAspect implements Ordered {

    @Autowired
    private TransactionConfigurator transactionConfigurator;

    @PostConstruct
    public void init() {
        TransactionManager transactionManager = transactionConfigurator.getTransactionManager();
        CompensableTransactionInterceptor compensableTransactionInterceptor = new CompensableTransactionInterceptor();
        compensableTransactionInterceptor.setTransactionManager(transactionManager);
        compensableTransactionInterceptor.setDelayCancelExceptions(transactionConfigurator.getRecoverConfig().getDelayCancelExceptions());
        this.setCompensableTransactionInterceptor(compensableTransactionInterceptor);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /*public void setTransactionConfigurator(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }*/
}
