package com.ljj.tcc.core.support;

/**
 * Created by liangjinjing on 11/20/15.
 */
public interface BeanFactory {
    <T> T getBean(Class<T> var1);

    <T> boolean isFactoryOf(Class<T> clazz);
}
