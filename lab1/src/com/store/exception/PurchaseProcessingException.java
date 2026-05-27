package com.store.exception;

public class PurchaseProcessingException extends RuntimeException {
    public PurchaseProcessingException(String message) {
        super(message);
    }
}