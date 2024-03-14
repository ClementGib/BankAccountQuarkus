package com.cdx.bas.domain.bank.customer.gender;

public enum Gender {
    MALE('M'),
    FEMALE('F'),
    OTHER('O');

    public final static char strMale = 'M';
    public final static char strFemale = 'F';
    public final static char strOther = 'O';
    private final char genderCode;

   Gender(Character maritalCode) {
        this.genderCode = maritalCode;
    }

    public char getGenderCode() {
        return genderCode;
    }
}
