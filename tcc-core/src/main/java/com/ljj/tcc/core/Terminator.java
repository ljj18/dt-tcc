
package com.ljj.tcc.core;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.ljj.tcc.api.TransactionContext;
import com.ljj.tcc.api.TransactionContextEditor;
import com.ljj.tcc.core.support.FactoryBuilder;
import com.ljj.tcc.core.utils.StringUtils;

/**
 * Created by liangjinjing on 10/30/15.
 */
public class Terminator implements Serializable {

    private static final long serialVersionUID = -164958655471605778L;

    private InvocationContext confirmInvocationContext;

    private InvocationContext cancelInvocationContext;

    public Terminator() {

    }

    public Object invoke(TransactionContext transactionContext, InvocationContext invocationContext,
        Class< ? extends TransactionContextEditor> transactionContextEditorClass) {
        if (StringUtils.isNotEmpty(invocationContext.getMethodName())) {

            try {

                Object target = FactoryBuilder.factoryOf(invocationContext.getTargetClass()).getInstance();

                Method method = null;

                method = target.getClass().getMethod(invocationContext.getMethodName(),
                    invocationContext.getParameterTypes());

                FactoryBuilder.factoryOf(transactionContextEditorClass).getInstance().set(transactionContext, target,
                    method, invocationContext.getArgs());

                return method.invoke(target, invocationContext.getArgs());

            } catch (Exception e) {
                throw new SystemException(e);
            }
        }
        return null;
    }

    public InvocationContext getConfirmInvocationContext() {
        return confirmInvocationContext;
    }

    public InvocationContext getCancelInvocationContext() {
        return cancelInvocationContext;
    }
}
