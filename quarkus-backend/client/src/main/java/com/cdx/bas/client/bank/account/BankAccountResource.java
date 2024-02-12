package com.cdx.bas.client.bank.account;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountControllerPort;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;
import com.cdx.bas.domain.transaction.TransactionServicePort;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;


@Path("/accounts")
public class BankAccountResource implements BankAccountControllerPort {
    @Inject
    BankAccountPersistencePort bankAccountPersistencePort;

    @Inject
    TransactionServicePort transactionServicePort;

    @GET()
    @Override
    public BankAccount findById(long id) {
        return bankAccountPersistencePort.findById(id).orElse(null);
    }

    @POST
    @Path("/{id}")
    @Override
    public BankAccount deposite(@PathParam("id") Long id, Long amount, String currency) {
        BankAccount currentAccount = null;
//        Optional<BankAccount> bankAccountOptional = bankAccountPersistencePort.findById(id);
//        if(bankAccountOptional.isPresent()) {
//            currentAccount = bankAccountOptional.get();
//            Transaction transaction = new Transaction(id,"EUR", new BigDecimal(amount), TransactionType.CREDIT);
//            currentAccount.getIssuedTransactions().add(transaction);
//            bankAccountPersistencePort.update(currentAccount);
//        }
        return currentAccount;
    }
}
