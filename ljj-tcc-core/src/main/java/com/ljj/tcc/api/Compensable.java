
package com.ljj.tcc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TCC策略注解, TCC服务方法必须加上此注解
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

    /**
     * 事务传播类型,默认必须事件
     */
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * 确认方法
     */
    String confirmMethod() default "";

    /**
     * 取消方法
     * @return
     */
    String cancelMethod() default "";

    /**
     * 
     * 事务上下文相关实例
     */
    Class< ? extends TransactionContextEditor> transactionContextEditor() default DefaultTransactionContextEditor.class;

    /**
     * 
     * 延迟取消异常集合
     */
    Class< ? extends Exception>[] delayCancelExceptions() default {};

    /**
     * 是否异步确认
     * @return
     */
    boolean asyncConfirm() default false;

    /**
     * 是否异步取消
     * @return
     */
    boolean asyncCancel() default false;
}