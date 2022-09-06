package com.cdx.bas.domain.bank.account;

import java.util.NoSuchElementException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.domain.bank.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class BankAccountServiceImpl implements BankAccountService {
    
    private static Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);
    
    @Inject
    BankAccountManager bankAccountManager;

    @Override
    public Transaction deposit(Transaction transaction) {
        try {
            BankAccount currentBankAccount = bankAccountManager.findById(transaction.accountId())
                    .orElseThrow(() -> new NoSuchElementException("bank account " + transaction.accountId() +" not found."));
            logger.info("Deposit for " +  transaction.accountId() + " of " + transaction.amount());
            currentBankAccount.getBalance().plus(Money.of(transaction.amount()));
            currentBankAccount.getHistory().add(transaction);
            bankAccountManager.update(currentBankAccount);
        } catch (NoSuchElementException exception) {
            
        }
        return transaction;
    }
}
