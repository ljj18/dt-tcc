package com.ljj.tcc.spring;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.ljj.tcc.core.interceptor.ResourceCoordinatorAspect;
import com.ljj.tcc.core.interceptor.ResourceCoordinatorInterceptor;
import com.ljj.tcc.core.support.TransactionConfigurator;

/**
 * Created by liangjinjing on 11/8/15.
 */
@Aspect
@Component
public class ConfigurableCoordinatorAspect extends ResourceCoordinatorAspect implements Ordered {

    @Autowired
    private TransactionConfigurator transactionConfigurator;

    public void init() {
        ResourceCoordinatorInterceptor resourceCoordinatorInterceptor = new ResourceCoordinatorInterceptor();
        resourceCoordinatorInterceptor.setTransactionManager(transactionConfigurator.getTransactionManager());
        this.setResourceCoordinatorInterceptor(resourceCoordinatorInterceptor);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    /*public void setTransactionConfigurator(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }*/
}
