package com.aiproxy.exception;

public class QuotaExceededException extends RuntimeException {

    public QuotaExceededException() {
        super("Monthly quota exhausted");
    }
}

