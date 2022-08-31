package com.cdx.bas.domain.bank.transaction;

import java.util.Queue;

public interface TransactionService {
    void processTransaction(Transaction transaction);
}
