package org.mengyun.tcctransaction.spring.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.ljj.tcc.core.support.BeanFactory;
import com.ljj.tcc.core.support.FactoryBuilder;

/**
 * Created by liangjinjing on 11/20/15.
 */
public class SpringPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();

        if (applicationContext.getParent() == null) {
            FactoryBuilder.registerBeanFactory(applicationContext.getBean(BeanFactory.class));
        }
    }
}
