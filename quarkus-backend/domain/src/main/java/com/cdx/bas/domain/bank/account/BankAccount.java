package com.cdx.bas.domain.bank.account;

import java.util.List;

import com.cdx.bas.domain.bank.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.customer.Customer;

public class BankAccount {

    private Long id;
    
    private AccountType type;
    
    private Money balance;
    
    private List<Customer> owners;
    
    private List<Transaction> pendingTransactions;
    
    private List<Transaction> history;

    public BankAccount(Long id, AccountType type, Money balance, List<Customer> owners) {
        this.id = id;
        this.type = type;
        this.balance = balance;
        this.owners = owners;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public List<Customer> getOwners() {
        return owners;
    }

    public void setOwners(List<Customer> owners) {
        this.owners = owners;
    }

    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(List<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public void setHistory(List<Transaction> history) {
        this.history = history;
    }
}
