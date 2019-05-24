package com.ljj.tcc.sample.exception;

/**
 * Created by liangjinjing on 11/21/17.
 */
public class InsufficientBalanceException extends RuntimeException {
    private static final long serialVersionUID = 6689953065473521009L;

    public InsufficientBalanceException() {

    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
