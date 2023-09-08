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

import static com.cdx.bas.domain.transaction.TransactionStatus.OUTSTANDING;
import static com.cdx.bas.domain.transaction.TransactionStatus.UNPROCESSED;

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
    public void processTransaction(Transaction transaction) {
        if (TransactionType.CREDIT.equals(transaction.getType())) {
            logger.info("Transaction " +  transaction.getAccountId() + " processing...");
            bankAccountService.deposit(transaction);
        }
    }

    @Override
    @Transactional(value = TxType.MANDATORY)
    public Transaction lockTransaction(Transaction transaction) {
        if (UNPROCESSED.equals(transaction.getStatus())) {
            transaction.setStatus(OUTSTANDING);
        } else {
            throw new TransactionException("Transaction is not longer unprocessed.");
        }
        return transactionRepository.update(transaction);
    }

    @Override
	public Transaction completeTransaction(Transaction transaction, Map<String, String> metadata) {
		transaction = new Transaction(transaction, TransactionStatus.COMPLETED, metadata);
		return transaction;
	}
}
