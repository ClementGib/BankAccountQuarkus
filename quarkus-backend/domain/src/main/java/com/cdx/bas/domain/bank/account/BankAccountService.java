package com.cdx.bas.domain.bank.account;

import com.cdx.bas.domain.bank.transaction.Transaction;

public interface BankAccountService {
    public Transaction deposit(Transaction transaction);
}
