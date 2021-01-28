package com.acmebank.AccountManager.exception;

public class ClientException extends RuntimeException {
    public ClientException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
