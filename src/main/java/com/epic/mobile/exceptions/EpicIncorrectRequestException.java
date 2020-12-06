package com.epic.mobile.exceptions;

import java.util.ArrayList;
import java.util.List;


public class EpicIncorrectRequestException
        extends RuntimeException {

    private final List<String> validationErrors;

    public EpicIncorrectRequestException() {
        super();
        this.validationErrors = new ArrayList<>();
    }

    public EpicIncorrectRequestException(List<String> validationErrors) {
        super();
        this.validationErrors = validationErrors;
    }

    public EpicIncorrectRequestException(String message) {
        super(message);
        this.validationErrors = new ArrayList<>();
    }

    public EpicIncorrectRequestException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public EpicIncorrectRequestException(String message, Throwable cause) {
        super(message, cause);
        this.validationErrors = new ArrayList<>();
    }

    public EpicIncorrectRequestException(String message, Throwable cause, List<String> validationErrors) {
        super(message, cause);
        this.validationErrors = validationErrors;
    }

    public EpicIncorrectRequestException(Throwable cause) {
        super(cause);
        this.validationErrors = new ArrayList<>();
    }

    public EpicIncorrectRequestException(Throwable cause, List<String> validationErrors) {
        super(cause);
        this.validationErrors = validationErrors;
    }

    protected EpicIncorrectRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.validationErrors = new ArrayList<>();
    }

    protected EpicIncorrectRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, List<String> validationErrors) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
