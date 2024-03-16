package com.cdx.bas.application.bank.transaction;

import com.cdx.bas.domain.bank.transaction.*;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.type.TransactionTypeProcessingServicePort;
import com.cdx.bas.domain.bank.transaction.validation.TransactionValidator;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.util.Set;

import static com.cdx.bas.domain.bank.transaction.type.TransactionType.CREDIT;
import static com.cdx.bas.domain.bank.transaction.type.TransactionType.DEPOSIT;

@RequestScoped
public class TransactionServiceImpl implements TransactionServicePort {

    private final Clock clock;

    private final TransactionPersistencePort transactionRepository;

    private final TransactionValidator transactionValidator;

    private final TransactionTypeProcessingServicePort transactionTypeProcessingService;

    @Inject
    public TransactionServiceImpl(TransactionPersistencePort transactionRepository,
                                  TransactionValidator transactionValidator,
                                  TransactionTypeProcessingServicePort transactionTypeProcessingService,
                                  Clock clock) {
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
        this.transactionTypeProcessingService = transactionTypeProcessingService;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Set<Transaction> getAll() {
        return transactionRepository.getAll();
    }

    @Override
    @Transactional
    public Set<Transaction> findAllByStatus(String status) throws IllegalArgumentException {
        TransactionStatus transactionStatus = TransactionStatus.fromString(status);
        return transactionRepository.findAllByStatus(transactionStatus);
    }

    @Override
    @Transactional
    public void createTransaction(NewTransaction newTransaction) throws TransactionException {
        Transaction transactionToCreate = Transaction.builder()
                .emitterAccountId(newTransaction.emitterAccountId())
                .receiverAccountId(newTransaction.receiverAccountId())
                .amount(newTransaction.amount())
                .currency(newTransaction.currency())
                .type(newTransaction.type())
                .status(TransactionStatus.UNPROCESSED)
                .date(clock.instant())
                .label(newTransaction.label())
                .metadata(newTransaction.metadata())
                .build();
        transactionValidator.validateNewTransaction(transactionToCreate);
        transactionRepository.create(transactionToCreate);
    }

    @Override
    @Transactional
    public Transaction findTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    @Override
    public void process(Transaction transaction) {
        if (CREDIT.equals(transaction.getType())) {
            transactionTypeProcessingService.credit(transaction);
        } else if (DEPOSIT.equals(transaction.getType())) {
            transactionTypeProcessingService.deposit(transaction);
        }
    }

    @Override
    public Transaction mergeTransactions(Transaction oldTransaction, Transaction newTransaction){
        oldTransaction.setId(newTransaction.getId());
        oldTransaction.setEmitterAccountId(newTransaction.getEmitterAccountId());
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
}
