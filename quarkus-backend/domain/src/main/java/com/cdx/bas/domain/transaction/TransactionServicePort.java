package com.cdx.bas.domain.transaction;

public interface TransactionServicePort {
    
    /**
     * process the transaction according to its values
     * 
     * @param transaction to process
     */
    void processTransaction(Transaction transaction);
}
