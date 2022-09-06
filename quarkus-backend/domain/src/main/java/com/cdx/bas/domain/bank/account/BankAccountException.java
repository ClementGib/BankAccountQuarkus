package com.cdx.bas.domain.bank.account;

public class BankAccountException extends RuntimeException {

    private static final long serialVersionUID = -5539383143022544288L;
    
    public BankAccountException(String errorMessage) {
        super(errorMessage);
    }
}
