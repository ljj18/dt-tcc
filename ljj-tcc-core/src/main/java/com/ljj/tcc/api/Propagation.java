package com.ljj.tcc.api;

/**
 * 事务传播行为
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public enum Propagation {
    /*
     * 如果存在一个事务，则支持当前事务。如果没有事务则开启一个新的事务
     */
    REQUIRED(0),
    /*
     * 如果存在一个事务，支持当前事务。如果没有事务，则非事务的执行
     */
    SUPPORTS(1),
    /*
     * 如果已经存在一个事务，支持当前事务。如果没有一个活动的事务，则抛出异常
     */
    MANDATORY(2),
    /*
     * 开启一个新的事务,如果一个事务已经存在，则先将这个存在的事务挂起
     */
    REQUIRES_NEW(3);

    private final int value;

    private Propagation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}