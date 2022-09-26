package com.cdx.bas.application.transaction;

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
    BankAccountServicePort bankAccountService;
    
    @Override
    public void processTransaction(Transaction transaction) throws IllegalStateException {
        transaction.validate();
        if (TransactionType.CREDIT.equals(transaction.type())) {
            logger.info("Credit transaction for " +  transaction.accountId() + " of " + transaction.amount());
            bankAccountService.deposit(transaction);
        }
    }
}
