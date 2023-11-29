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

    @NotNull(message = "id must not be null.")
    @Min(value = 1, message = "id must be positive and greater than 0.")
    private Long id;

    @NotNull(message = "accountId must not be null.")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && Objects.equals(accountId, that.accountId) && Objects.equals(amount, that.amount) && Objects.equals(currency, that.currency) && type == that.type && status == that.status && Objects.equals(date, that.date) && Objects.equals(label, that.label) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, amount, currency, type, status, date, label, metadata);
    }
}
