package com.tua.apps.library.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
