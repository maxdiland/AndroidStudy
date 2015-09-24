package com.gmail.maxdiland.drebedengireports.exception.validation;

/**
 * author Max Diland
 */
public class SearchFormValidationException extends Exception {
    public SearchFormValidationException() {
    }

    public SearchFormValidationException(String detailMessage) {
        super(detailMessage);
    }

    public SearchFormValidationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SearchFormValidationException(Throwable throwable) {
        super(throwable);
    }
}
