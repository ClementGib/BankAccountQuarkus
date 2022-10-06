package com.cdx.bas.client.bank.account;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountControllerPort;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionServicePort;
import com.cdx.bas.domain.transaction.TransactionType;


@Path("/account")
public class BankAccountResource implements BankAccountControllerPort{

    @Inject
    BankAccountPersistencePort bankAccountRepository;
    
    @Inject
    TransactionServicePort transactionService;
    
    @GET
    @Override
    public BankAccount findById(long id) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(id);
        return bankAccountOptional.get();
    }

    @PUT
    @Path("/{id}")
    @Override
    public BankAccount deposite(@PathParam("id") Long id, Long amount) {
        BankAccount currentAccount = null;
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(id);
        if(bankAccountOptional.isPresent()) {
            currentAccount = bankAccountOptional.get();
            Transaction transaction = transactionService.createNewTransaction(id, amount, TransactionType.CREDIT);
            currentAccount.getTransactions().add(transaction);
            bankAccountRepository.update(currentAccount);
        }
        return currentAccount;
    }

}
