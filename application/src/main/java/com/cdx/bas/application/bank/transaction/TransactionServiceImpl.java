package com.cdx.bas.application.bank.transaction;

import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionException;
import com.cdx.bas.domain.bank.transaction.TransactionPersistencePort;
import com.cdx.bas.domain.bank.transaction.TransactionServicePort;
import com.cdx.bas.domain.transaction.*;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.type.TransactionTypeProcessingServicePort;
import com.cdx.bas.domain.bank.transaction.validation.TransactionValidator;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.Set;

import static com.cdx.bas.domain.bank.transaction.type.TransactionType.CREDIT;
import static com.cdx.bas.domain.bank.transaction.type.TransactionType.DEPOSIT;

@RequestScoped
public class TransactionServiceImpl implements TransactionServicePort {

    @Inject
    TransactionPersistencePort transactionRepository;

    @Inject
    TransactionValidator transactionValidator;

    @Inject
    TransactionTypeProcessingServicePort transactionTypeProcessingService;

    @Override
    public Set<Transaction> getAll() {
        return transactionRepository.getAll();
    }

    @Override
    public Set<Transaction> findAllByStatus(String status) throws IllegalArgumentException {
        TransactionStatus transactionStatus = TransactionStatus.fromString(status);
        return transactionRepository.findAllByStatus(transactionStatus);
    }

    @Override
    @Transactional(Transactional.TxType.MANDATORY)
    public void createTransaction(Transaction newTransaction) throws TransactionException {
        transactionValidator.validateNewTransaction(newTransaction);
        transactionRepository.create(newTransaction);
    }

    @Override
    public Transaction mergeTransactions(Transaction oldTransaction, Transaction newTransaction){
        oldTransaction.setId(newTransaction.getId());
        oldTransaction.setSenderAccountId(newTransaction.getSenderAccountId());
        oldTransaction.setReceiverAccountId(newTransaction.getReceiverAccountId());
        oldTransaction.setAmount(newTransaction.getAmount());
        oldTransaction.setCurrency(newTransaction.getCurrency());
        oldTransaction.setType(newTransaction.getType());
        oldTransaction.setStatus(newTransaction.getStatus());
        oldTransaction.setDate(newTransaction.getDate());
        oldTransaction.setLabel(newTransaction.getLabel());
        oldTransaction.setMetadata(newTransaction.getMetadata());
        return oldTransaction;
    }

    @Override
    public void process(Transaction transaction) {
        if (CREDIT.equals(transaction.getType())) {
            transactionTypeProcessingService.credit(transaction);
        } else if (DEPOSIT.equals(transaction.getType())) {
            transactionTypeProcessingService.deposit(transaction);
        }
    }
}
