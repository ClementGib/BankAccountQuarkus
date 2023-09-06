package com.cdx.bas.application.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;
import com.vladmihalcea.hibernate.type.json.JsonType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(schema = "basapp", name = "transactions", uniqueConstraints = @UniqueConstraint(columnNames = "transaction_id"))
@NamedQueries(@NamedQuery(name = "TransactionEntity.findUnprocessed", query = "SELECT t FROM TransactionEntity t WHERE t.status = :status ORDER BY t.date ASC"))
@TypeDef(name = "jsonb", typeClass = JsonType.class)
public class TransactionEntity extends PanacheEntityBase {

    @Id
    @Column(name = "transaction_id", nullable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "bank_accounts_transactions", joinColumns = @JoinColumn(name = "transaction_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private BankAccountEntity account;

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

    @Type(type = "jsonb")
    @Column(name = "metadatas", columnDefinition = "jsonb",  nullable = true)
    private String metadatas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BankAccountEntity getAccount() {
        return account;
    }

    public void setAccount(BankAccountEntity account) {
        this.account = account;
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

    public String getMetadatas() {
        return metadatas;
    }

    public void setMetadatas(String metadatas) {
        this.metadatas = metadatas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, amount, date, id, label, metadatas, status, type);
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
        return Objects.equals(account, other.account) && Objects.equals(amount, other.amount)
                && Objects.equals(date, other.date) && id == other.id && Objects.equals(label, other.label)
                && Objects.equals(metadatas, other.metadatas) && status == other.status && type == other.type;
    }
}
