package com.cdx.bas.client.bank.account;

import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

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

    @POST
    @Path("/{id}")
    @Override
    public BankAccount deposite(@PathParam("id") Long id, Long amount, String currency) {
        BankAccount currentAccount = null;
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(id);
        if(bankAccountOptional.isPresent()) {
            currentAccount = bankAccountOptional.get();
            Transaction transaction = new Transaction(id, currency, amount, TransactionType.CREDIT);
            currentAccount.getTransactions().add(transaction);
            bankAccountRepository.update(currentAccount);
        }
        return currentAccount;
    }

}
