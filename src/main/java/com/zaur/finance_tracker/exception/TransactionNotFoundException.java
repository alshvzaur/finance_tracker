package com.zaur.finance_tracker.exception;

public class TransactionNotFoundException  extends RuntimeException {

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
