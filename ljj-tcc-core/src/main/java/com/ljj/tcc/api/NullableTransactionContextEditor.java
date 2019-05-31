/**
 * 文件名称:          		NullableTransactionContextEditor.java
 * 版权所有@ 2019-2020 	无锡爱超信息技术有限公司
 * 编译器:           		JDK1.8
 */

package com.ljj.tcc.api;

import java.lang.reflect.Method;

/**
 * 事务Context接口空实现
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public class NullableTransactionContextEditor implements TransactionContextEditor {

    @ Override
    public TransactionContext get(Object target, Method method, Object[] args) {
        return null;
    }

    @ Override
    public void set(TransactionContext transactionContext, Object target, Method method, Object[] args) {

    }
}
