package com.cdx.bas.domain.bank.account.checking;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.money.Amount;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;

/**
 * Checking account (transaction account/current account)
 */
public class CheckingBankAccount extends BankAccount {
    
    @NotNull(message="balance must not be null.")
    @Amount(min=-600, max=100000, message="balance amount must be between -600 and 100000.")
    protected Money balance;
    
    public CheckingBankAccount() {
        super(AccountType.CHECKING);
    }

    public CheckingBankAccount(Long id, Money balance, List<Long> customersId, Set<Transaction> transactions) {
        super(id, AccountType.CHECKING, balance, customersId, transactions);
        this.balance = balance;
        System.out.println(super.balance);
        System.out.println(this.balance);
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        super.balance = balance;
        this.balance = balance;
    }
}
