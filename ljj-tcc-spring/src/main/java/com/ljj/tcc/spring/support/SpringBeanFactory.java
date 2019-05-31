package com.ljj.tcc.spring.support;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.ljj.tcc.core.support.BeanFactory;
import com.ljj.tcc.core.support.FactoryBuilder;

/**
 * Created by liangjinjing on 11/22/15.
 */
@Component("springBeanFactory")
public class SpringBeanFactory implements BeanFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        FactoryBuilder.registerBeanFactory(this);
    }

    @ SuppressWarnings("rawtypes")
    @Override
    public boolean isFactoryOf(Class clazz) {
        @ SuppressWarnings("unchecked")
        Map map = this.applicationContext.getBeansOfType(clazz);
        return map.size() > 0;
    }

    @Override
    public <T> T getBean(Class<T> var1) {
        return this.applicationContext.getBean(var1);
    }
}
