package com.cdx.bas.application.bank.account;

import com.cdx.bas.domain.bank.account.*;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionServicePort;
import com.cdx.bas.domain.utils.ExchangeRateUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequestScoped
public class BankAccountServiceImpl implements BankAccountServicePort {
    
    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);
    
    @Inject
    BankAccountPersistencePort bankAccountRepository;
    
    @Inject
    BankAccountValidator bankAccountValidator;
    
    @Inject
    TransactionServicePort transactionService;

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public Transaction deposit(Transaction transaction) {
        Map<String, String> metadata = new HashMap<>();
        try {
            BankAccount senderBankAccount = bankAccountRepository.findById(transaction.getSenderAccountId())
                    .orElseThrow(() -> new NoSuchElementException("Sender bank account " + transaction.getSenderAccountId() + " is not found."));

            BankAccount receiverBankAccount = bankAccountRepository.findById(transaction.getReceiverAccountId())
                    .orElseThrow(() -> new NoSuchElementException("Receiver bank account " + transaction.getReceiverAccountId() + " is not found."));

            Transaction currentTransaction = transactionService.setAsOutstanding(transaction);

            logger.info("Bank account " + currentTransaction.getSenderAccountId()
                    + " process deposit transaction " + currentTransaction.getId()
                    + " with amount " + currentTransaction.getAmount()
                    + " to bank account " + currentTransaction.getReceiverAccountId());

            metadata.put("sender_amount_before", senderBankAccount.getBalance().getAmount().toString());
            metadata.put("receiver_amount_before", receiverBankAccount.getBalance().getAmount().toString());
            creditBankAccountFromTransaction(senderBankAccount, receiverBankAccount, currentTransaction);
            bankAccountValidator.validateBankAccount(senderBankAccount);
            bankAccountValidator.validateBankAccount(receiverBankAccount);
            metadata.put("sender_amount_after", senderBankAccount.getBalance().getAmount().toString());
            metadata.put("receiver_amount_after", receiverBankAccount.getBalance().getAmount().toString());

            Transaction completedTransaction = transactionService.setAsCompleted(currentTransaction, metadata);
            BankAccount updatedBankAccount = addTransaction(senderBankAccount, completedTransaction);

            logger.info("Bank account " + updatedBankAccount.getId() + " deposit transaction " + + currentTransaction.getId() + " completed.");
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

    private void creditBankAccountFromTransaction(BankAccount senderBankAccount, BankAccount receiverBankAccount, Transaction transactionToCredit) {
        BigDecimal euroAmount = ExchangeRateUtils.getEuroAmountFrom(transactionToCredit.getCurrency(), transactionToCredit.getAmount());
        senderBankAccount.getBalance().minus(Money.of(euroAmount));
        receiverBankAccount.getBalance().plus(Money.of(euroAmount));
    }

    private BankAccount addTransaction(BankAccount currentBankAccount, Transaction newTransaction) {
        Optional<Transaction> optionalStoredTransaction = currentBankAccount.getIssuedTransactions().stream()
                .filter(transaction -> transaction.getId().equals(newTransaction.getId()))
                .findFirst();

        if (optionalStoredTransaction.isPresent()) {
            Transaction mergedTransaction = transactionService.mergeTransactions(optionalStoredTransaction.get(), newTransaction);
            currentBankAccount.getIssuedTransactions().removeIf(existingTransaction -> existingTransaction.getId().equals(mergedTransaction.getId()));
            currentBankAccount.getIssuedTransactions().add(mergedTransaction);
        } else {
            currentBankAccount.getIssuedTransactions().add(newTransaction);
        }
        return bankAccountRepository.update(currentBankAccount);
    }
}
