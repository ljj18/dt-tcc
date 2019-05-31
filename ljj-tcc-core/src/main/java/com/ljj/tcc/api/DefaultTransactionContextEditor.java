/**
 * 文件名称:          		DefaultTransactionContextEditor.java
 * 版权所有@ 2019-2020 	无锡爱超信息技术有限公司
 * 编译器:           		JDK1.8
 */

package com.ljj.tcc.api;

import java.lang.reflect.Method;

/**
 * 事务Context操作默认实现
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:35
 * 
 */
public class DefaultTransactionContextEditor implements TransactionContextEditor {

    @ Override
    public TransactionContext get(Object target, Method method, Object[] args) {
        int position = getTransactionContextParamPosition(method.getParameterTypes());
        if (position >= 0) {
            return (TransactionContext)args[position];
        }

        return null;
    }

    @ Override
    public void set(TransactionContext transactionContext, Object target, Method method, Object[] args) {
        int position = getTransactionContextParamPosition(method.getParameterTypes());
        if (position >= 0) {
            args[position] = transactionContext;
        }
    }

    /**
     * 
     * @param parameterTypes
     * @return
     */
    private int getTransactionContextParamPosition(Class< ? >[] parameterTypes) {
        int position = -1;
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].equals(TransactionContext.class)) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 
     * @param args
     * @return
     */
    public TransactionContext getTransactionContextFromArgs(Object[] args) {
        TransactionContext transactionContext = null;
        for (Object arg : args) {
            if (arg != null && TransactionContext.class.isAssignableFrom(arg.getClass())) {
                transactionContext = (TransactionContext)arg;
                break;
            }
        }
        return transactionContext;
    }
}
