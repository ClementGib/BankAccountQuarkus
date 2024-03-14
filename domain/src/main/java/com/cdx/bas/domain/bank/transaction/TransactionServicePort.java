package com.cdx.bas.domain.bank.transaction;

import java.util.Set;

public interface TransactionServicePort {

    /**
     * find all transactions
     *
     * @return Set with all the transactions
     */
    public Set<Transaction> getAll();

    /**
     * find all transactions by status
     *
     * @return Set with all the transactions by status
     */
    public Set<Transaction> findAllByStatus(String status);


    /**
     * add current transaction
     *
     * @param newTransaction to add
     */
    void createTransaction(NewTransaction newTransaction);

    /**
     * merge two transactions
     *
     * @param oldTransaction to merge with next
     * @param newTransaction to merge with previous
     */
    Transaction mergeTransactions(Transaction oldTransaction, Transaction newTransaction);

    /**
     * find Transaction from id
     *
     * @param transactionId
     * @return Transaction found
     */
    Transaction findTransaction(Long transactionId);

    /**
     * Process the transaction depending on its type
     * 
     * @param transaction to process
     */
    void process(Transaction transaction);
}
