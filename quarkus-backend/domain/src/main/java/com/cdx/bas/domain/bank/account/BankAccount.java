package com.cdx.bas.domain.bank.account;

import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.*;

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

    @NotNull(message="issued transactions must not be null.")
    private Set<Transaction> issuedTransactions = new HashSet<>();
    
    public BankAccount(AccountType type) {
        this.type = type;
    }

    public BankAccount(Long id, AccountType type, Money balance, List<Long> customersId, Set<Transaction> issuedTransactions, Set<Transaction> receivedTransactions) {
      this.id = id;
      this.type = type;
      this.customersId = customersId;
      this.issuedTransactions = issuedTransactions;
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

    public void setCustomersId(List<Long> customersId) {
        this.customersId = customersId;
    }

    public Set<Transaction> getIssuedTransactions() {
        return issuedTransactions;
    }

    public void setIssuedTransactions(Set<Transaction> issuedTransactions) {
        this.issuedTransactions = issuedTransactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id) && type == that.type
                && Objects.equals(balance, that.balance)
                && Objects.equals(customersId, that.customersId)
                && Objects.equals(issuedTransactions, that.issuedTransactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, balance, customersId, issuedTransactions);
    }
}
