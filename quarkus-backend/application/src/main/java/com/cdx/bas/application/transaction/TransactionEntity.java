package com.cdx.bas.application.transaction;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(schema = "basapp", name = "transactions", uniqueConstraints = @UniqueConstraint(columnNames = "transaction_id"))
@NamedQueries(@NamedQuery(
        name = "TransactionEntity.findUnprocessed", query = "SELECT t FROM TransactionEntity t WHERE t.status = :status ORDER BY t.date ASC"))
public class TransactionEntity extends PanacheEntityBase {

    @Id
    @Column(name = "transaction_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_transaction_id_seq_gen")
    @SequenceGenerator(name = "transactions_transaction_id_seq_gen", sequenceName = "transactions_transaction_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "bank_accounts_transactions", joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "sender_account_id"))
    private BankAccountEntity senderAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "bank_accounts_transactions", joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "receiver_account_id"))
    private BankAccountEntity receiverAccount;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "label", nullable = false)
    private String label;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb",  nullable = true)
    private String metadata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BankAccountEntity getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(BankAccountEntity senderAccount) {
        this.senderAccount = senderAccount;
    }

    public BankAccountEntity getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(BankAccountEntity receiverAccount) {
        receiverAccount = receiverAccount;
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

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderAccount, receiverAccount, amount, date, id, label, metadata, status, type);
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
        TransactionEntity other = (TransactionEntity) obj;
        return Objects.equals(senderAccount, other.senderAccount) && Objects.equals(receiverAccount, other.receiverAccount)
                && Objects.equals(amount, other.amount)
                && Objects.equals(date, other.date)
                && id == other.id
                && Objects.equals(label, other.label)
                && Objects.equals(metadata, other.metadata)
                && status == other.status
                && type == other.type;
    }
}
