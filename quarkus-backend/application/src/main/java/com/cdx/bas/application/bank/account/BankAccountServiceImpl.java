package com.cdx.bas.application.bank.account;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountException;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;
import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.bank.account.BankAccountValidator;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionException;
import com.cdx.bas.domain.transaction.TransactionServicePort;
import com.cdx.bas.domain.transaction.TransactionStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class BankAccountServiceImpl implements BankAccountServicePort {
    
    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);
    
    @Inject
    BankAccountPersistencePort BankAccountRepository;
    
    @Inject
    BankAccountValidator bankAccountValidator;
    
    @Inject
    TransactionServicePort transactionService;

    @Override
    public Transaction deposit(Transaction transaction) {
        Map<String, String> metadata = new HashMap<>();
        try {
            BankAccount currentBankAccount = BankAccountRepository.findById(transaction.getAccountId())
                    .orElseThrow(() -> new NoSuchElementException("bank account " + transaction.getAccountId() + " not found."));
            logger.info("BankAccount " + transaction.getAccountId() + " transaction deposit " + transaction.getId() + " for amount "+ transaction.getAmount());
            
            metadata.put("amount_before", currentBankAccount.getBalance().getAmount().toString());
            currentBankAccount.getBalance().plus(Money.of(transaction.getAmount()));
            bankAccountValidator.validateBankAccount(currentBankAccount);
            metadata.put("amount_after", currentBankAccount.getBalance().getAmount().toString());
            
            Transaction currentTransaction = currentBankAccount.getTransaction(transaction.getId());
            Transaction completedTransaction = transactionService.completeTransaction(currentTransaction, metadata);
            currentBankAccount.addTransaction(completedTransaction);
            BankAccountRepository.update(currentBankAccount);
            return currentTransaction;
            
        } catch (NoSuchElementException exception) {
            logger.error("Transaction " + transaction.getId() + " deposit error for amount "+ transaction.getAmount() + ": " + exception.getMessage());
            metadata.put("error", exception.getMessage());
            return new Transaction(transaction, TransactionStatus.ERROR, metadata);
        } catch (BankAccountException exception) {
            metadata.put("error", exception.getMessage());
            logger.error("Transaction " + transaction.getId() + " deposit refused for amount "+ transaction.getAmount() + ": " + exception.getMessage());
            return new Transaction(transaction, TransactionStatus.REFUSED, metadata);
        }
    }
}
