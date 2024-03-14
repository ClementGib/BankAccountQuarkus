package com.cdx.bas.domain.bank.customer.maritalstatus;

public enum MaritalStatus {
    SINGLE('S'),
    MARRIED('M'),
    WIDOWED('W'),
    DIVORCED('D'),
    PACS('P');

    public final static char strSingle = 'S';
    public final static char strMarried = 'M';
    public final static char strWidowed = 'W';
    public final static char strDivorced= 'D';
    public final static char strPacs = 'P';

    private final char maritalCode;

    MaritalStatus(Character maritalCode) {
        this.maritalCode = maritalCode;
    }

    public char getMaritalCode() {
        return maritalCode;
    }
}
