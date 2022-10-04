package com.cdx.bas.domain.bank.account.mma;

import java.util.Set;

import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.money.Amount;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;

/**
 * Money Market Account
 */
public class MMABankAccount extends BankAccount {
    
    @Amount(min=1000, max=250000, message="balance amount must be between 1000 and 250000")
    protected Money balance;
    
    public MMABankAccount() {
        super(AccountType.MMA);
    }

    public MMABankAccount(Long id, Money balance, Set<Long> customersId, Set<Transaction> transactions, Set<Transaction> history) {
        super(id, AccountType.MMA, balance, customersId, transactions, history);
        this.balance = balance;
    }
}
