package com.cdx.bas.domain.transaction;

import java.util.Map;

public interface TransactionServicePort {

    /**
     * Process the transaction according to its values
     * 
     * @param transaction to process
     */
    void process(Transaction transaction);

    /**
     * Set transaction to outstanding with additional metadata and avoid multiple process
     *
     * @param transaction to set as OUTSTANDING
     * @return outstanding transaction
     */
    Transaction setAsOutstanding(Transaction transaction);
    
    /**
     * Complete processed transaction 
     * 
     * @param completedTransaction to set as COMPLETED
     * @param metadata with information to set to the transaction
     */
    Transaction setAsCompleted(Transaction completedTransaction, Map<String, String> metadata);

    /**
     * Set transaction on error with additional metadata
     *
     * @param erroredTransaction to set as ERROR
     * @param metadata with error to set to the transaction
     */
    Transaction setAsError(Transaction erroredTransaction, Map<String, String> metadata);

    /**
     * Set transaction refused with additional metadata
     *
     * @param refusedTransaction to set as REFUSED
     * @param metadata with information to set to the transaction
     */
    Transaction setAsRefused(Transaction refusedTransaction, Map<String, String> metadata);


    /**
     * merge two transaction and return old transaction merged with new
     *
     * @param oldTransaction to adapt with new transaction
     * @param newTransaction to merge with old transaction
     */
    Transaction mergeTransactions(Transaction oldTransaction, Transaction newTransaction);

}
