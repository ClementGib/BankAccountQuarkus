package com.cdx.bas.domain.error;

import java.io.Serial;

public class CurrencyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4661867226831802143L;
    public CurrencyException(String errorMessage) {
        super(errorMessage);
    }
}
