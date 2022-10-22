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
	Optional<Transaction> findById(long id);
    
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
	Transaction create(Transaction transaction);
    
    /**
     * update the current Transaction
     * 
     * @param Transaction to update
     * @return updated Transaction
     */
	Transaction update(Transaction transaction);
    
    /**
     * delete the current Transaction
     * 
     * @param Transaction id to remove
     */
	Optional<Transaction> deleteById(long id);
}
