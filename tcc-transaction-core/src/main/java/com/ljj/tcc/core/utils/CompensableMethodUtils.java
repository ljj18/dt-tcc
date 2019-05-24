package com.ljj.tcc.core.utils;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.ljj.tcc.api.Compensable;
import com.ljj.tcc.api.Propagation;
import com.ljj.tcc.api.TransactionContext;
import com.ljj.tcc.core.common.MethodRole;

/**
 * Created by liangjinjing on 11/21/15.
 */
public class CompensableMethodUtils {

    public static Method getCompensableMethod(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) (pjp.getSignature())).getMethod();

        if (method.getAnnotation(Compensable.class) == null) {
            try {
                method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
        return method;
    }

    public static MethodRole calculateMethodType(Propagation propagation, boolean isTransactionActive, TransactionContext transactionContext) {

        if ((propagation.equals(Propagation.REQUIRED) && !isTransactionActive && transactionContext == null) ||
                propagation.equals(Propagation.REQUIRES_NEW)) {
            return MethodRole.ROOT;
        } else if ((propagation.equals(Propagation.REQUIRED) || propagation.equals(Propagation.MANDATORY)) && !isTransactionActive && transactionContext != null) {
            return MethodRole.PROVIDER;
        } else {
            return MethodRole.NORMAL;
        }
    }

    public static int getTransactionContextParamPosition(Class<?>[] parameterTypes) {

        int position = -1;

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].equals(TransactionContext.class)) {
                position = i;
                break;
            }
        }
        return position;
    }
}
