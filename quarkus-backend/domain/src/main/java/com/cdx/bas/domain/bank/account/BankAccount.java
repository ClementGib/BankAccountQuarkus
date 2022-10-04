package com.cdx.bas.domain.bank.account;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;

public abstract class BankAccount {

	@Min(value=1, message="id must be positive and greater than 0.")
    protected long id;
    
	@NotNull(message="type must not be null.")
	protected AccountType type;
    
	@NotNull(message="balance must not be null.")
	protected Money balance;
    
	@NotNull(message="customersId must not be null.")
	@Size(min=1, message="customersId must contains at least 1 customer id.")
	protected Set<Long> customersId = new HashSet<>();
    
	@NotNull(message="transactions must not be null.")
	protected Set<Transaction> transactions = new HashSet<>();

	@NotNull(message="history must not be null.")
	protected Set<Transaction> history = new HashSet<>();
    
    public BankAccount(AccountType type) {
        this.type = type;
    }

    public BankAccount(Long id, AccountType type, Money balance, Set<Long> customersId, Set<Transaction> transactions, Set<Transaction> history) {
      this.id = id;
      this.type = type;
      this.balance = balance;
      this.customersId = customersId;
      this.transactions = transactions;
      this.history = history;
      this.balance = balance;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
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
