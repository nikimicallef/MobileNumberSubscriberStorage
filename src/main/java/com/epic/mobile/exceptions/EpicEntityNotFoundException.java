package com.epic.mobile.exceptions;

public class EpicEntityNotFoundException
        extends RuntimeException {

    public EpicEntityNotFoundException() {
        super();
    }

    public EpicEntityNotFoundException(String message) {
        super(message);
    }

    public EpicEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EpicEntityNotFoundException(Throwable cause) {
        super(cause);
    }

    protected EpicEntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
