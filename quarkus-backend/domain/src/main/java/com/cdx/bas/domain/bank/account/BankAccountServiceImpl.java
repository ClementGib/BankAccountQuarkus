package com.cdx.bas.domain.bank.account;

import java.util.NoSuchElementException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.domain.bank.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class BankAccountServiceImpl implements BankAccountService {
    
    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);
    
    @Inject
    BankAccountManager bankAccountManager;

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
