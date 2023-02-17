package com.cdx.bas.domain.bank.account;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionException;

public abstract class BankAccount {

    @NotNull(message="id must not be null.")
	@Min(value=1, message="id must be positive and greater than 0.")
    protected Long id;
    
	@NotNull(message="type must not be null.")
	protected AccountType type;
    
	@NotNull(message="balance must not be null.")
	@Valid
	protected Money balance;
    
	@NotNull(message="customersId must not be null.")
	@Size(min=1, message="customersId must contains at least 1 customer id.")
	protected List<Long> customersId = new ArrayList<>();
    
	@NotNull(message="transactions must not be null.")
	protected Set<Transaction> transactions = new HashSet<>();
    
    public BankAccount(AccountType type) {
        this.type = type;
    }

    public BankAccount(Long id, AccountType type, Money balance, List<Long> customersId, Set<Transaction> transactions) {
      this.id = id;
      this.type = type;
      this.balance = balance;
      this.customersId = customersId;
      this.transactions = transactions;
      this.balance = balance;
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

    public List<Long> getCustomersId() {
		return customersId;
	}

	public void setCustomersId(List<Long> customerId) {
		this.customersId = customerId;
	}

	public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    public Transaction getTransaction(Long transactionId) {
        return transactions.stream()
                .filter(transaction -> transaction.getId().equals(transactionId))
                .findFirst().orElseThrow(() -> new TransactionException("Transaction " + transactionId + " not found in the bank account."));
        }

    public void addTransaction(Transaction newTransaction) {
        this.transactions.stream()
        .filter(transaction -> transaction.getId().equals(newTransaction.getId()))
        .findFirst()
        .ifPresentOrElse(
                transaction -> {
                    transactions.remove(transaction);
                    transactions.add(newTransaction);
                }, () -> transactions.add(newTransaction));
    }
    

	@Override
	public int hashCode() {
		return Objects.hash(balance, customersId, id, transactions, type);
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
		return Objects.equals(balance, other.balance) 
		        && Objects.equals(customersId, other.customersId)
		        && Objects.equals(id, other.id)
				&& Objects.equals(transactions, other.transactions) 
				&& type == other.type;
	}
}
