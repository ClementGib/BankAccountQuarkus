package com.cdx.bas.domain.transaction;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Transaction implements Comparable<Transaction> {
	
	@Min(value=1, message="id must be positive and greater than 0.")
    private long id;
    
	@NotNull(message="gender must not be null.")
    private long accountId;
    
	@Min(value=1, message="amount must be positive and greater than 0.")
    private long amount;
    
	@NotNull(message="type must not be null.")
    private TransactionType type; 
    
	@NotNull(message="status must not be null.")
    private TransactionStatus status;

	@NotNull(message="date must not be null.")
    private Instant date;
    
	@NotNull(message="label must not be null.")
    private String label;
	
	private Map<String, String> metadatas = new HashMap<>();

    public Transaction() {
		super();
	}
    
    public Transaction(Transaction transaction, TransactionStatus status) {
    	this.id = transaction.id;
    	this.accountId = transaction.accountId;
    	this.amount = transaction.amount;
    	this.type = transaction.type;
    	this.status = status;
    	this.date = transaction.date;
    	this.label = transaction.label;
    }
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public Map<String, String> getMetadatas() {
        return metadatas;
    }

    public void setMetadatas(Map<String, String> metadatas) {
        this.metadatas = metadatas;
    }

    @Override
    public int compareTo(Transaction transactionToCompar) {
        return this.getDate().compareTo(transactionToCompar.getDate());
    }

	@Override
    public int hashCode() {
        return Objects.hash(accountId, amount, date, id, label, metadatas, status, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Transaction other = (Transaction) obj;
        return accountId == other.accountId && amount == other.amount && Objects.equals(date, other.date)
                && id == other.id && Objects.equals(label, other.label) && Objects.equals(metadatas, other.metadatas)
                && status == other.status && type == other.type;
    }
}
