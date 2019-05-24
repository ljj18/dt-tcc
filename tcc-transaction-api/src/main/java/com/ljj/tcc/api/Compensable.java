
package com.ljj.tcc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO: 文件注释
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
@ Retention(RetentionPolicy.RUNTIME)
@ Target({ElementType.METHOD})
public @interface Compensable {

    public Propagation propagation() default Propagation.REQUIRED;

    public String confirmMethod() default "";

    public String cancelMethod() default "";

    public Class< ? extends TransactionContextEditor> transactionContextEditor() default DefaultTransactionContextEditor.class;

    public Class< ? extends Exception>[] delayCancelExceptions() default {};

    public boolean asyncConfirm() default false;

    public boolean asyncCancel() default false;
}