package com.cdx.bas.domain.transaction;

public interface TransactionServicePort {
    
    /**
     * process the transaction according to its values
     * 
     * @param transaction to process
     */
    void processTransaction(Transaction transaction);
    
    /**
     * create transaction for a specific account and with an amount of money
     * 
     * @param accountId of the transaction
     * @param amount to add the account
     */
    public Transaction createNewTransaction(long accountId, long amount, TransactionType type);
}
