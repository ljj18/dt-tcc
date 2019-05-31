package com.ljj.tcc.api;

import java.lang.reflect.Method;

/**
 * 事务Context操作接口
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public interface TransactionContextEditor {

    /**
     * 获取事务Context
     * @param target
     * @param method
     * @param args
     * @return
     */
    TransactionContext get(Object target, Method method, Object[] args);
    
    /**
     * 设置事务Context
     * @param transactionContext
     * @param target
     * @param method
     * @param args
     */
    void set(TransactionContext transactionContext, Object target, Method method, Object[] args);

}
