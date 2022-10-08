package com.cdx.bas.domain.transaction;

public class TransactionException extends RuntimeException {

    private static final long serialVersionUID = 9064189216525457809L;

	public TransactionException(String errorMessage) {
        super(errorMessage);
    }
}
