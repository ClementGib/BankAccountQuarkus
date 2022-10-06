package com.cdx.bas.domain.bank.account.saving;

import java.util.Set;

import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.money.Amount;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;

/**
 * Saving Account (French Livret A)
 */
public class SavingBankAccount extends BankAccount {
    
    @Amount(min=1, max=22950, message="balance amount must be between 1 and 22950")
    protected Money balance;
    
    public SavingBankAccount() {
        super(AccountType.SAVING);
    }

    public SavingBankAccount(Long id, Money balance, Set<Long> customersId, Set<Transaction> transactions, Set<Transaction> history) {
        super(id, AccountType.SAVING, balance, customersId, transactions, history);
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
