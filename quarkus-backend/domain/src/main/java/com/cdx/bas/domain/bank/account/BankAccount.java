package com.cdx.bas.domain.bank.account;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;

import net.dv8tion.jda.api.MessageBuilder;

public class BankAccount {

    private Long id;
    
    private AccountType type;
    
    private Money balance;
    
    private Set<Long> customersId;
    
    private Set<Transaction> transactions = new HashSet<>();
    
    private Set<Transaction> history = new HashSet<>();
    
    public BankAccount() {
        super();
    }

    public BankAccount(Long id, AccountType type, Money balance, Set<Long> ownersId, Set<Transaction> transactions, Set<Transaction> history) {
        this.id = id;
        this.type = type;
        this.balance = balance;
        this.customersId = ownersId;
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

    public Set<Long> getCustomersId() {
		return customersId;
	}

	public void setCustomersId(Set<Long> customerId) {
		this.customersId = customerId;
	}

	public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Set<Transaction> getHistory() {
        return history;
    }

    public void setHistory(Set<Transaction> history) {
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
        
        if (customersId == null) {
            messageBuilder.append("ownersId must not be null.\n");
        } else if (customersId.isEmpty()) {
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

	@Override
	public int hashCode() {
		return Objects.hash(balance, customersId, history, id, transactions, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BankAccount other = (BankAccount) obj;
		return Objects.equals(balance, other.balance) && Objects.equals(customersId, other.customersId)
				&& Objects.equals(history, other.history) && Objects.equals(id, other.id)
				&& Objects.equals(transactions, other.transactions) && type == other.type;
	}
}
