package com.cdx.bas.domain.transaction;

import java.io.Serial;

public class TransactionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9064189216525457809L;

	public TransactionException(String errorMessage) {
        super(errorMessage);
    }
}
