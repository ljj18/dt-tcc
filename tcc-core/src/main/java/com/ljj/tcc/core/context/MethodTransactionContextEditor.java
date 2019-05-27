package com.ljj.tcc.core.context;

import java.lang.reflect.Method;

import com.ljj.tcc.api.TransactionContext;
import com.ljj.tcc.api.TransactionContextEditor;
import com.ljj.tcc.core.utils.CompensableMethodUtils;

/**
 * this class is replaced by org.mengyun.tcctransaction.api.Compensable.DefaultTransactionContextEditor
 */
@Deprecated
public class MethodTransactionContextEditor implements TransactionContextEditor {

    @Override
    public TransactionContext get(Object target, Method method, Object[] args) {
        int position = CompensableMethodUtils.getTransactionContextParamPosition(method.getParameterTypes());

        if (position >= 0) {
            return (TransactionContext) args[position];
        }
        
        return null;
    }

    @Override
    public void set(TransactionContext transactionContext, Object target, Method method, Object[] args) {

        int position = CompensableMethodUtils.getTransactionContextParamPosition(method.getParameterTypes());
        if (position >= 0) {
            args[position] = transactionContext;
        }
    }
}
