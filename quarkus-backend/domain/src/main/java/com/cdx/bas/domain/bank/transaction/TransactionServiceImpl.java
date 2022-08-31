package com.cdx.bas.domain.bank.transaction;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.domain.bank.account.BankAccountService;
import com.cdx.bas.domain.bank.money.Money;

@RequestScoped
public class TransactionServiceImpl implements TransactionService {

    @Inject
    BankAccountService bankAccountService;
    
    @Override
    public void processTransaction(Transaction transaction) {
        transaction.validate();
        if (TransactionType.CREDIT.equals(transaction.type())) {
            bankAccountService.deposit(transaction.accountId(), Money.of(transaction.amount()));
        }
    }
}
