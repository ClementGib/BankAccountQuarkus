package com.cdx.bas.application.bank.account;

import java.util.NoSuchElementException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountException;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;
import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class BankAccountServiceImpl implements BankAccountServicePort {
    
    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);
    
    @Inject
    BankAccountPersistencePort bankAccountManager;

    @Override
    public Transaction deposit(Transaction transaction) {
        try {
            BankAccount currentBankAccount = bankAccountManager.findById(transaction.accountId())
                    .orElseThrow(() -> new NoSuchElementException("bank account " + transaction.accountId() +" not found."));
            logger.info("Deposit for " +  transaction.accountId() + " of " + transaction.amount());
            
            currentBankAccount.getBalance().plus(Money.of(transaction.amount()));
            Transaction completedTransaction = new Transaction(transaction, TransactionStatus.COMPLETED);
            currentBankAccount.getHistory().add(completedTransaction);
            bankAccountManager.update(currentBankAccount);
            return completedTransaction;
            
        } catch (NoSuchElementException exception) {
            logger.error("Deposit error for " +  transaction.accountId() + " of " + transaction.amount());
            return new Transaction(transaction, TransactionStatus.ERROR);
        } catch (BankAccountException exception) {
            logger.error("Deposit error for " +  transaction.accountId() + " of " + transaction.amount());
            return new Transaction(transaction, TransactionStatus.REFUSED);
        }
    }
}
