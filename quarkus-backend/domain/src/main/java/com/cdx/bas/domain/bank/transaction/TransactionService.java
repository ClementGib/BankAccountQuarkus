package com.cdx.bas.domain.bank.transaction;

import java.util.Queue;

public interface TransactionService {
    Queue<Transaction> getUnprocessedTransactions();
}
