package com.cdx.bas.domain.bank.transaction;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.domain.bank.account.BankAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class TransactionServiceImpl implements TransactionService {

    private static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    
    @Inject
    BankAccountService bankAccountService;
    
    @Override
    public void processTransaction(Transaction transaction) throws IllegalStateException {
        transaction.validate();
        if (TransactionType.CREDIT.equals(transaction.type())) {
            logger.info("Credit transaction for " +  transaction.accountId() + " of " + transaction.amount());
            bankAccountService.deposit(transaction);
        }
    }
}
