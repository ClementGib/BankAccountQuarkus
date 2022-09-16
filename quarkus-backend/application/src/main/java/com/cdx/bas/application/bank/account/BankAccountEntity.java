package com.cdx.bas.application.bank.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cdx.bas.domain.bank.transaction.Transaction;

@Entity
@Table(schema = "basapp", name = "bank_accounts", uniqueConstraints = @UniqueConstraint(columnNames = "account_id"))
public class BankAccountEntity {

    @Id
    @Column(name = "account_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_accounts_account_id_seq_gen")
    @SequenceGenerator(name = "bank_accounts_account_id_seq_gen", sequenceName = "bank_accounts_account_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;
    
    @Column(name = "type", nullable = false)
    private String type;
    
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    
    @ManyToMany(mappedBy = "cutomers")
    private List<Long> ownersId;
    
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "bank_accounts_transactions", joinColumns = @JoinColumn(name = "transaction_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Transaction> transactions = new ArrayList<>();
    
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "bank_accounts_history", joinColumns = @JoinColumn(name = "transaction_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Transaction> history = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Long> getOwnersId() {
        return ownersId;
    }

    public void setOwnersId(List<Long> ownersId) {
        this.ownersId = ownersId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public void setHistory(List<Transaction> history) {
        this.history = history;
    }
}
