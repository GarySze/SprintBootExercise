package com.acmebank.AccountManager.exception;

public class ServerException extends RuntimeException {
    public ServerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}