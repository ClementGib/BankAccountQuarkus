package com.cdx.bas.application.transaction;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionServicePort;
import com.cdx.bas.domain.transaction.TransactionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class TransactionServiceImpl implements TransactionServicePort {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    
    @Inject
    TransactionRepository transactionRepository;
    
    @Inject
    BankAccountServicePort bankAccountService;
    
    public List<Transaction> findAll() {
//        return transactionRepository.findAll().stream().map(null);
        return new ArrayList<>();
    }
    
    @Override
    public void processTransaction(Transaction transaction) throws IllegalStateException {
        if (TransactionType.CREDIT.equals(transaction.getType())) {
            logger.info("Transaction " +  transaction.getAccountId() + " processing...");
            bankAccountService.deposit(transaction);
        }
    }
}
