package com.cdx.bas.domain.bank.account.saving;

import jakarta.validation.constraints.NotNull;

import com.cdx.bas.domain.bank.account.type.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.money.Amount;
import com.cdx.bas.domain.money.Money;

/**
 * Saving Account (French Livret A)
 */
public class SavingBankAccount extends BankAccount {
    
    @NotNull(message="balance must not be null.")
    @Amount(min=1, max=22950, message="balance amount must be between 1 and 22950.")
    protected Money balance;
    
    public SavingBankAccount() {
        super(AccountType.SAVING);
    }

//    public SavingBankAccount(Long id, Money balance, List<Long> customersId, Set<Transaction> transactions) {
//        super(id, AccountType.SAVING, balance, customersId, transactions);
//        this.balance = balance;
//    }
    
    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        super.balance = balance;
        this.balance = balance;
    }
}
