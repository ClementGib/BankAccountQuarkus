package com.cdx.bas.domain.bank.account;

import com.cdx.bas.domain.transaction.Transaction;

/**
 * Make a deposit from a transaction
 * 
 * @param deposit transaction
 * @return transaction processing status
 */
public interface BankAccountServicePort {
    public Transaction deposit(Transaction transaction);
}
