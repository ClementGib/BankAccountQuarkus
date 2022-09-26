package com.cdx.bas.domain.transaction;

import java.util.Queue;

public interface TransactionPersistencePort {
    
    /**
     * get all unprocessed transactions in a queue
     * 
     * @return queue with all the unprocessed transactions
     */
    Queue<Transaction> getUnprocessedTransactions();
}
