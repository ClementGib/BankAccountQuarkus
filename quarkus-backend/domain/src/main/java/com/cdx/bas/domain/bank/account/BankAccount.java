package com.cdx.bas.domain.bank.account;

import java.util.ArrayList;
import java.util.List;

import com.cdx.bas.domain.bank.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;

import net.dv8tion.jda.api.MessageBuilder;

public class BankAccount {

    private Long id;
    
    private AccountType type;
    
    private Money balance;
    
    private List<Long> ownersId;
    
    private List<Transaction> transactions = new ArrayList<>();
    
    private List<Transaction> history = new ArrayList<>();
    
    public BankAccount() {
        super();
    }

    public BankAccount(Long id, AccountType type, Money balance, List<Long> ownersId, List<Transaction> transactions, List<Transaction> history) {
        this.id = id;
        this.type = type;
        this.balance = balance;
        this.ownersId = ownersId;
        this.transactions = transactions;
        this.history = history;
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

    public List<Long> getOwnersId() {
        return ownersId;
    }

    public void setOwnersId(List<Long> ownersId) {
        this.ownersId = ownersId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public void setHistory(List<Transaction> history) {
        this.history = history;
    }
    
    public void validate() {
        MessageBuilder messageBuilder = new MessageBuilder();
        
        if (id == null) {
            messageBuilder.append("id must not be null.\n");
        } else if (id < 1) {
            messageBuilder.append("id must be positive and higher than 0.\n");
        }
        
        if (type == null) {
            messageBuilder.append("type must not be null.\n");
        }
        
        if (balance == null) {
            messageBuilder.append("balance must not be null.\n");
        }
        
        if (ownersId == null) {
            messageBuilder.append("ownersId must not be null.\n");
        } else if (ownersId.isEmpty()) {
            messageBuilder.append("ownersId must contain at least 1 owner id.\n");
        }
        
        if (transactions == null) {
            messageBuilder.append("transactions must not be null.\n");
        }
        
        if (history == null) {
            messageBuilder.append("history must not be null.\n");
        }
        
        if (messageBuilder.length() > 0) {
            throw new IllegalStateException(messageBuilder.build().getContentRaw());
        }
    }
}
