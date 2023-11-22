package com.cdx.bas.application.bank.account;

import com.cdx.bas.domain.bank.account.*;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionServicePort;
import com.cdx.bas.domain.utils.ExchangeRateUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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
    @Transactional(value = TxType.REQUIRES_NEW)
    public Transaction deposit(Transaction transaction) {
        Map<String, String> metadata = new HashMap<>();
        try {
            transactionService.setAsOutstanding(transaction);
            BankAccount currentBankAccount = BankAccountRepository.findById(transaction.getAccountId())
                    .orElseThrow(() -> new NoSuchElementException("bank account " + transaction.getAccountId() + " not found."));

            logger.info("BankAccount " + transaction.getAccountId() + " process transaction deposit " + transaction.getId() + " for amount "+ transaction.getAmount());
            metadata.put("amount_before", currentBankAccount.getBalance().getAmount().toString());
            creditBankAccountFromTransaction(currentBankAccount, transaction);
            bankAccountValidator.validateBankAccount(currentBankAccount);
            metadata.put("amount_after", currentBankAccount.getBalance().getAmount().toString());

            Transaction completedTransaction = transactionService.setAsCompleted(transaction, metadata);
            addTransaction(currentBankAccount, completedTransaction);
            BankAccountRepository.update(currentBankAccount);
            logger.info("BankAccount " + transaction.getAccountId() + " transaction deposit " + + transaction.getId() + " completed.");
            return completedTransaction;
        } catch (NoSuchElementException exception) {
            logger.error("Transaction " + transaction.getId() + " deposit error for amount "+ transaction.getAmount() + ": " + exception.getMessage());
            metadata = new HashMap<>();
            metadata.put("error", exception.getMessage());
            return transactionService.setAsError(transaction, metadata);
        } catch (BankAccountException exception) {
            metadata.put("error", exception.getMessage());
            logger.error("Transaction " + transaction.getId() + " deposit refused for amount "+ transaction.getAmount() + ": " + exception.getMessage());
            return transactionService.setAsRefused(transaction, metadata);
        }
    }

    private void creditBankAccountFromTransaction(BankAccount currentBankAccount, Transaction transactionToCredit) {
        BigDecimal euroAmount = ExchangeRateUtils.getEuroAmountFrom(transactionToCredit.getCurrency(), transactionToCredit.getAmount());
        currentBankAccount.getBalance().plus(Money.of(euroAmount));
    }

    private void addTransaction(BankAccount currentBankAccount, Transaction newTransaction) {
        currentBankAccount.getTransactions().stream()
                .filter(transaction -> transaction.getId().equals(newTransaction.getId()))
                .findFirst()
                .ifPresentOrElse(
                        transaction -> {
                            transactionService.mergeTransactions(transaction, newTransaction);
                        }, () -> currentBankAccount.getTransactions().add(newTransaction));
    }
}
