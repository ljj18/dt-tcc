package com.ljj.tcc.core.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.ljj.tcc.api.Compensable;
import com.ljj.tcc.api.Propagation;
import com.ljj.tcc.api.TransactionContext;
import com.ljj.tcc.api.UniqueIdentity;
import com.ljj.tcc.core.common.MethodRole;
import com.ljj.tcc.core.support.FactoryBuilder;

/**
 */
public class CompensableMethodContext {

    ProceedingJoinPoint pjp = null;

    Method method = null;

    Compensable compensable = null;

    Propagation propagation = null;

    TransactionContext transactionContext = null;

    public CompensableMethodContext(ProceedingJoinPoint pjp) {
        this.pjp = pjp;
        this.method = getCompensableMethod();
        this.compensable = method.getAnnotation(Compensable.class);
        this.propagation = compensable.propagation();
        this.transactionContext = FactoryBuilder.factoryOf(compensable.transactionContextEditor()).getInstance().get(pjp.getTarget(), method, pjp.getArgs());

    }

    public Compensable getAnnotation() {
        return compensable;
    }

    public Propagation getPropagation() {
        return propagation;
    }

    public TransactionContext getTransactionContext() {
        return transactionContext;
    }

    public Method getMethod() {
        return method;
    }

    public Object getUniqueIdentity() {
        Annotation[][] annotations = this.getMethod().getParameterAnnotations();

        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType().equals(UniqueIdentity.class)) {

                    Object[] params = pjp.getArgs();
                    Object unqiueIdentity = params[i];

                    return unqiueIdentity;
                }
            }
        }

        return null;
    }


    private Method getCompensableMethod() {
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

    public MethodRole getMethodRole(boolean isTransactionActive) {
        if ((propagation.equals(Propagation.REQUIRED) && !isTransactionActive && transactionContext == null) ||
                propagation.equals(Propagation.REQUIRES_NEW)) {
            return MethodRole.ROOT;
        } else if ((propagation.equals(Propagation.REQUIRED) || propagation.equals(Propagation.MANDATORY)) && !isTransactionActive && transactionContext != null) {
            return MethodRole.PROVIDER;
        } else {
            return MethodRole.NORMAL;
        }
    }

    public Object proceed() throws Throwable {
        return this.pjp.proceed();
    }
}