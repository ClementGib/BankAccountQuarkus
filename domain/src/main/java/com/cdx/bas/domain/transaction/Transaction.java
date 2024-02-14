package com.cdx.bas.domain.transaction;

import com.cdx.bas.domain.validator.ValidCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Transaction implements Comparable<Transaction> {

    @NotNull(message = "id must not be null.")
    @Min(value = 1, message = "id must be positive and greater than 0.")
    private Long id;

    @NotNull(message = "accountId must not be null.")
    private Long senderAccountId;

    @NotNull(message = "accountId must not be null.")
    private Long receiverAccountId;

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

    public Transaction(Long id, Long senderAccountId, Long receiverAccountId, BigDecimal amount, String currency, TransactionType type, TransactionStatus status, Instant date, String label, Map<String, String> metadata) {
        this.id = id;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.status = status;
        this.date = date;
        this.label = label;
        this.metadata = metadata;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Long getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(Long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
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
        return Objects.equals(id, that.id)
                && Objects.equals(senderAccountId, that.senderAccountId)
                && Objects.equals(receiverAccountId, that.receiverAccountId)
                && Objects.equals(amount, that.amount)
                && Objects.equals(currency, that.currency)
                && type == that.type
                && status == that.status
                && Objects.equals(date, that.date)
                && Objects.equals(label, that.label)
                && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderAccountId, receiverAccountId, amount, currency, type, status, date, label, metadata);
    }
}