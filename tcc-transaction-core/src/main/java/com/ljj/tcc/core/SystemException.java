package com.ljj.tcc.core;

/**
 * Created by liangjinjing on 6/1/16.
 */
public class SystemException extends RuntimeException {


    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable e) {
        super(e);
    }

    public SystemException(String message, Throwable e) {
        super(message, e);
    }
}
