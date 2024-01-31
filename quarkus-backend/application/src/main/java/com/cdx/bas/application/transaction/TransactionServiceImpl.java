package com.cdx.bas.application.transaction;

import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.transaction.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cdx.bas.domain.transaction.TransactionStatus.*;

@RequestScoped
public class TransactionServiceImpl implements TransactionServicePort {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    
    @Inject
    TransactionPersistencePort transactionRepository;
    
    @Inject
    BankAccountServicePort bankAccountService;
    
    public List<Transaction> findAll() {
//        return transactionRepository.findAll().stream().map(null);
        return new ArrayList<>();
    }
    
    @Override
    public void process(Transaction transaction) {
        if (TransactionType.CREDIT.equals(transaction.getType())) {
            logger.info("Transaction " +  transaction.getId() + " processing...");
            bankAccountService.deposit(transaction);
        }
    }

    @Override
    @Transactional(value = TxType.MANDATORY)
    public Transaction setAsOutstanding(Transaction transaction) {
        if (UNPROCESSED.equals(transaction.getStatus())) {
            transaction.setStatus(OUTSTANDING);
        } else {
            throw new TransactionException("Transaction is not longer unprocessed.");
        }
        return transactionRepository.update(transaction);
    }

    @Override
    @Transactional(value = TxType.MANDATORY)
	public Transaction setAsCompleted(Transaction completedTransaction, Map<String, String> metadata) {
        setState(completedTransaction, metadata, COMPLETED);
		return transactionRepository.update(completedTransaction);
	}

    @Override
    @Transactional(value = TxType.MANDATORY)
    public Transaction setAsError(Transaction erroredTransaction, Map<String, String> metadata) {
        setState(erroredTransaction, metadata, ERROR);
        return transactionRepository.update(erroredTransaction);
    }

    @Override
    @Transactional(value = TxType.MANDATORY)
    public Transaction setAsRefused(Transaction refusedTransaction, Map<String, String> metadata) {
        setState(refusedTransaction, metadata, REFUSED);
        return transactionRepository.update(refusedTransaction);
    }

    private void setState(Transaction refusedTransaction, Map<String, String> metadata, TransactionStatus status) {
        refusedTransaction.setMetadata(metadata);
        refusedTransaction.setStatus(status);
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
}
