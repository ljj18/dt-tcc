package com.ljj.tcc.api;

/**
 * 事务阶段(Try, confirm, cancel)枚举
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public enum TransactionPhase {

    /**
     * 偿试阶段
     */
    TRYING(1),
    /**
     * 确认阶段 
     */
    CONFIRMING(2),
    /**
     * 回滚阶段
     */
    CANCELLING(3);

    private int id;

     TransactionPhase(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TransactionPhase valueOf(int id) {

        switch (id) {
            case 1:
                return TRYING;
            case 2:
                return CONFIRMING;
            default:
                return CANCELLING;
        }
    }

}
