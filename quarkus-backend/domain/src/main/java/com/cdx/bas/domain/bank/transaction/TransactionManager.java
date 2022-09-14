package com.cdx.bas.domain.bank.transaction;

import java.util.Queue;

public interface TransactionManager {
    
    /**
     * get all unprocessed transactions in a queue
     * 
     * @return queue with all the unprocessed transactions
     */
    Queue<Transaction> getUnprocessedTransactions();
}
