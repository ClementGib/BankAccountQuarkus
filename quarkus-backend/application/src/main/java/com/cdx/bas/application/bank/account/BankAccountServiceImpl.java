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
    BankAccountPersistencePort BankAccountRepository;

    @Override
    public Transaction deposit(Transaction transaction) {
        try {
            BankAccount currentBankAccount = BankAccountRepository.findById(transaction.getAccountId())
                    .orElseThrow(() -> new NoSuchElementException("bank account " + transaction.getAccountId() +" not found."));
            logger.info("Deposit for " +  transaction.getAccountId() + " of " + transaction.getAmount());
            
            currentBankAccount.getBalance().plus(Money.of(transaction.getAmount()));
            Transaction completedTransaction = new Transaction(transaction, TransactionStatus.COMPLETED);
            currentBankAccount.getHistory().add(completedTransaction);
            BankAccountRepository.update(currentBankAccount);
            return completedTransaction;
            
        } catch (NoSuchElementException exception) {
            logger.error("Deposit error for " +  transaction.getAccountId() + " of " + transaction.getAmount());
            return new Transaction(transaction, TransactionStatus.ERROR);
        } catch (BankAccountException exception) {
            logger.error("Deposit error for " +  transaction.getAccountId() + " of " + transaction.getAmount());
            return new Transaction(transaction, TransactionStatus.REFUSED);
        }
    }
}
