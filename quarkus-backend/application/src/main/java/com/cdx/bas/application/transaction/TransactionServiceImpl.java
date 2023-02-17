package com.cdx.bas.application.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionException;
import com.cdx.bas.domain.transaction.TransactionPersistencePort;
import com.cdx.bas.domain.transaction.TransactionServicePort;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Transactional(value = TxType.REQUIRES_NEW)
    public void processTransaction(Transaction transaction) {
        if (TransactionType.CREDIT.equals(transaction.getType())) {
            logger.info("Transaction " +  transaction.getAccountId() + " processing...");
            bankAccountService.deposit(transaction);
        }
    }

	@Override
	public Transaction completeTransaction(Transaction transaction, Map<String, String> metadatas) {
		transaction = new Transaction(transaction, TransactionStatus.COMPLETED, metadatas);
		return transaction;
	}
}
