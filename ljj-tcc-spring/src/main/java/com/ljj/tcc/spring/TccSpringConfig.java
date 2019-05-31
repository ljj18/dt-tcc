/**
 * 文件名称:          		TccSpringConfig.java
 * 版权所有@ 2019-2020 	无锡爱超信息技术有限公司
 * 编译器:           		JDK1.8
 */

package com.ljj.tcc.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.ljj.tcc.core.recover.TransactionRecovery;
import com.ljj.tcc.core.support.TransactionConfigurator;
import com.ljj.tcc.spring.support.SpringBeanFactory;

/**
 * Tcc Spring相关的配置
 * 
 * Version		1.0.0      
 * 
 * @author		liangjinjing
 * 
 * Date			2019-05-28 17:36
 * 
 */
@Configuration
public class TccSpringConfig {

    /*@Bean(name = "springBeanFactory")
    public SpringBeanFactory getSpringBeanFactory() {
        return new SpringBeanFactory();
    }*/
    
   /* @Bean(name="recoverConfig")
    public RecoverConfig getRecoverConfig() {
        *//**
         * 可能根据自己需要定义此Bean
         *//*
        return DefaultRecoverConfig.INSTANCE;
    }*/
    
    @Bean(name="transactionRecovery")
    public TransactionRecovery getTransactionRecovery(TransactionConfigurator transactionConfigurator){
        return new TransactionRecovery(transactionConfigurator);
    }
    
    @Bean
    public SchedulerFactoryBean getSchedulerFactoryBean() {
        return new SchedulerFactoryBean();
    }
}
