package com.cdx.bas.domain.bank.account.mma;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.money.Amount;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;

/**
 * Money Market Account
 */
public class MMABankAccount extends BankAccount {
    
    @NotNull(message="balance must not be null.")
    @Amount(min=1000, max=250000, message="balance amount must be between 1000 and 250000.")
    protected Money balance;
    
    public MMABankAccount() {
        super(AccountType.MMA);
    }

    public MMABankAccount(Long id, Money balance, List<Long> customersId, Set<Transaction> transactions) {
        super(id, AccountType.MMA, balance, customersId, transactions);
        this.balance = balance;
    }
    
    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        super.balance = balance;
        this.balance = balance;
    }
}
