package com.cdx.bas.domain.bank.transaction;

import java.util.Queue;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class TransactionServiceImpl implements TransactionService {

    @Override
    public Queue<Transaction> getUnprocessedTransactions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void processTransaction(Transaction transaction) {
        // TODO Auto-generated method stub
        
    }

}
