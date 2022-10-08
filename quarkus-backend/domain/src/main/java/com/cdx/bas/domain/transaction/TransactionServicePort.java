package com.cdx.bas.domain.transaction;

import java.util.Map;
import java.util.Set;

public interface TransactionServicePort {
    
    /**
     * process the transaction according to its values
     * 
     * @param transaction to process
     */
    void processTransaction(Transaction transaction);
    
    /**
     * find transaction into a Set collection
     * 
     * @param transactionId id of the transaction to find
     * @param transactions Set where find the transaction
     */
    Transaction extractTransactionFromCollection(Long transactionId, Set<Transaction> transactions);
    
    /**
     * Complete processed transaction 
     * 
     * @param transaction to complete
     * @param metadatas to set to the transaction
     */
    Transaction completeTransaction(Transaction transaction, Map<String, String> metadatas);
    
}
