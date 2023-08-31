package com.cdx.bas.domain.transaction;

import java.util.Map;
import java.util.Set;

public interface TransactionServicePort {
    
    /**
     * Process the transaction according to its values
     * 
     * @param transaction to process
     */
    void processTransaction(Transaction transaction);

    /**
     * Lock transaction to avoid multiple process
     *
     * @param transaction
     * @return transaction outstanding locked
     */
    Transaction lockTransaction(Transaction transaction);
    
    /**
     * Complete processed transaction 
     * 
     * @param transaction to complete
     * @param metadata to set to the transaction
     */
    Transaction completeTransaction(Transaction transaction, Map<String, String> metadata);
    
}
