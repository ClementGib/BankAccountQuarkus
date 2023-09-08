package com.cdx.bas.domain.transaction;

import com.cdx.bas.domain.validator.ValidCurrency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Transaction implements Comparable<Transaction> {

    @NotNull(message="id must not be null.")
    @Min(value = 1, message = "id must be positive and greater than 0.")
    private Long id;

    @NotNull(message = "gender must not be null.")
    private Long accountId;

    @Min(value = 1, message = "amount must be positive and greater than 0.")
    private BigDecimal amount;

    @NotNull(message = "currency must not be null.")
    @ValidCurrency
    private String currency;

    @NotNull(message = "type must not be null.")
    private TransactionType type;

    @NotNull(message = "status must not be null.")
    private TransactionStatus status;

    @NotNull(message = "date must not be null.")
    private Instant date;

    @NotNull(message = "label must not be null.")
    private String label;

    private Map<String, String> metadata = new HashMap<>();

    public Transaction() {
        super();
    }

    public Transaction(long accountId, String currency, BigDecimal amount, TransactionType type) {
        this.id = accountId;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.status = TransactionStatus.UNPROCESSED;
        this.date = Instant.now();
    }

    public Transaction(Transaction transaction, TransactionStatus status, Map<String, String> metadata) {
        this.id = transaction.id;
        this.accountId = transaction.accountId;
        this.amount = transaction.amount;
        this.currency = transaction.currency;
        this.type = transaction.type;
        this.status = status;
        this.date = transaction.date;
        this.label = transaction.label;
        this.metadata.putAll(metadata);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public int compareTo(Transaction transactionToCompar) {
        return this.getDate().compareTo(transactionToCompar.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, amount, date, id, label, metadata, status, type);
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
        return Objects.equals(accountId, other.accountId) && amount == other.amount
                && Objects.equals(date, other.date)
                && Objects.equals(id, other.id) && Objects.equals(label, other.label)
                && Objects.equals(metadata, other.metadata)
                && status == other.status && type == other.type;
    }
}
