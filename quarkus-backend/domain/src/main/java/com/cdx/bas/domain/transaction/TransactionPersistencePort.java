package com.cdx.bas.domain.transaction;

import java.util.Optional;
import java.util.Queue;

public interface TransactionPersistencePort {
    
    /**
     * find Transaction from its id
     * 
     * @param id of Transaction
     * @return <Optional>Transaction if id corresponding or not to a Transaction
     */
    public Optional<Transaction> findById(long id);
    
    /**
     * find all unprocessed transactions in a queue
     * 
     * @return queue with all the unprocessed transactions
     */
    Queue<Transaction> findUnprocessedTransactions();
    
    /**
     * create the current Transaction
     * 
     * @param Transaction to create
     * @return created Transaction
     */
    public Transaction create(Transaction transaction);
    
    /**
     * update the current Transaction
     * 
     * @param Transaction to update
     * @return updated Transaction
     */
    public Transaction update(Transaction transaction);
    
    /**
     * delete the current Transaction
     * 
     * @param Transaction to remove
     */
    public void remove(Transaction transaction);
}
