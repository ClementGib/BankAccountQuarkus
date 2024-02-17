package com.cdx.bas.domain.transaction;

public enum TransactionStatus {
    UNPROCESSED,
    OUTSTANDING,
    WAITING,
    COMPLETED,
    REFUSED,
    ERROR;


    public static TransactionStatus fromString(String status) throws IllegalArgumentException{
        for (TransactionStatus value : values()) {
            if (value.name().equalsIgnoreCase(status)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }

}
