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
     * Make a deposit on bank account
     *
     * @param id of BankAccount
     * @param depositTransaction to add to the BankAccount
     * @return Response with status corresponding to transaction validation or not
     */
    public Response deposit(Long id, Transaction depositTransaction);

}
