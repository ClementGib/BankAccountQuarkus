package com.cdx.bas.domain.bank.transaction;

public interface TransactionService {
    /**
     * process the transaction according to its values
     * 
     * @param transaction
     */
    void processTransaction(Transaction transaction);
}
