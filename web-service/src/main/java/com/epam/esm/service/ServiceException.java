package com.epam.esm.service;

public class ServiceException extends RuntimeException {
    private final String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public ServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ServiceException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }
}
