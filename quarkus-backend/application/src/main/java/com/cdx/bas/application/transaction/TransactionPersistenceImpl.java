package com.cdx.bas.application.transaction;

import java.util.PriorityQueue;
import java.util.Queue;

import javax.enterprise.context.RequestScoped;

import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionPersistencePort;

@RequestScoped
public class TransactionPersistenceImpl implements TransactionPersistencePort{

    @Override
    public Queue<Transaction> getUnprocessedTransactions() {
        return new PriorityQueue<>();
    }
}
