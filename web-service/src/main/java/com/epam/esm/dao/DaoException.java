package com.epam.esm.dao;

public class DaoException extends Exception {
    private final String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public DaoException(String errorCode) {
        this.errorCode = errorCode;
    }

    public DaoException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DaoException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
