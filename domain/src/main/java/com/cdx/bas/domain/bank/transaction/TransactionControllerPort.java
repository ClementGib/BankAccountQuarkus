package com.cdx.bas.domain.bank.transaction;

import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.Set;

public interface TransactionControllerPort {

    /**
     * Find all Transaction
     *
     * @return all Transaction found
     */
    public Set<Transaction> getAll();

    /**
     * Find all Transaction with matching status
     *
     * @param status of Transaction
     * @return all Transaction corresponding to the status
     */
    public Set<Transaction> getAllByStatus(@PathParam("status") String status) ;

    /**
     * Find Transaction from its id
     *
     * @param id of Transaction
     * @return Transaction corresponding to the id
     */
    public Transaction findById(long id);

    /**
     * Create a new digital transaction
     *
     * @param newTransaction to add to a BankAccount
     * @return Response with status corresponding to transaction validation or not
     */
    public Response addDigitalTransaction(NewTransaction newTransaction);

    /**
     * process a new transaction
     *
     * @param newTransaction to process for a BankAccount
     * @return Response with status corresponding to transaction validation or not
     */
    public Response processCashTransaction(NewTransaction newTransaction);

}
