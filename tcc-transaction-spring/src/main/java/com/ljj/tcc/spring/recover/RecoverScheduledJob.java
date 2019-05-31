package com.ljj.tcc.spring.recover;

import javax.annotation.PostConstruct;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.ljj.tcc.core.SystemException;
import com.ljj.tcc.core.recover.TransactionRecovery;
import com.ljj.tcc.core.support.TransactionConfigurator;

/**
 * Created by liangjinjing on 6/2/16.
 */
@Component
public class RecoverScheduledJob {

    @Autowired
    private TransactionConfigurator transactionConfigurator;
    
    /*
     * 
     */
    private TransactionRecovery transactionRecovery;
    
    /*
     * 
     */
    private Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {
            scheduler = new SchedulerFactoryBean().getScheduler();
            transactionRecovery = new TransactionRecovery(transactionConfigurator);
            MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
            jobDetail.setTargetObject(transactionRecovery);
            jobDetail.setTargetMethod("startRecover");
            jobDetail.setName("transactionRecoveryJob");
            jobDetail.setConcurrent(false);
            jobDetail.afterPropertiesSet();

            CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
            cronTrigger.setBeanName("transactionRecoveryCronTrigger");
            cronTrigger.setCronExpression(transactionConfigurator.getRecoverConfig().getCronExpression());
            cronTrigger.setJobDetail(jobDetail.getObject());
            cronTrigger.afterPropertiesSet();

            scheduler.scheduleJob(jobDetail.getObject(), cronTrigger.getObject());
            scheduler.start();

        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public void setTransactionRecovery(TransactionRecovery transactionRecovery) {
        this.transactionRecovery = transactionRecovery;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setTransactionConfigurator(TransactionConfigurator transactionConfigurator) {
        this.transactionConfigurator = transactionConfigurator;
    }
}
