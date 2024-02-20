package com.cdx.bas.client.bank.account;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountControllerPort;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionServicePort;
import com.cdx.bas.domain.transaction.validation.TransactionValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/accounts")
@ApplicationScoped
public class BankAccountResource implements BankAccountControllerPort {
    @Inject
    BankAccountPersistencePort bankAccountPersistencePort;

    @Inject
    TransactionValidator transactionValidator;

    @Inject
    TransactionServicePort transactionServicePort;

    @GET()
    @Override
    public BankAccount findById(long id) {
        return bankAccountPersistencePort.findById(id).orElse(null);
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Transaction deposite(@PathParam("id") Long id, Transaction depositTransaction) {
        transactionValidator.validateNewTransaction(depositTransaction);

        return depositTransaction;
    }
}
