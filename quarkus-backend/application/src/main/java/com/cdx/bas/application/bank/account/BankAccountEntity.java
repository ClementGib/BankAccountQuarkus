package com.cdx.bas.application.bank.account;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cdx.bas.application.customer.CustomerEntity;
import com.cdx.bas.application.transaction.TransactionEntity;
import com.cdx.bas.domain.bank.account.AccountType;

@Entity
@Table(schema = "basapp", name = "bank_accounts", uniqueConstraints = @UniqueConstraint(columnNames = "account_id"))
public class BankAccountEntity {

    @Id
    @Column(name = "account_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_accounts_account_id_seq_gen")
    @SequenceGenerator(name = "bank_accounts_account_id_seq_gen", sequenceName = "bank_accounts_account_id_seq", allocationSize = 1, initialValue = 1)
    private long id;
    
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType type;
    
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "accounts")
    private Set<CustomerEntity> customers = new HashSet<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "bank_accounts_transactions", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "transaction_id"))
    @OrderBy("date")
    private Set<TransactionEntity> transactions = new HashSet<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "bank_accounts_history", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "transaction_id"))
    @OrderBy("date")
    private Set<TransactionEntity> history = new HashSet<>();

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Set<CustomerEntity> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<CustomerEntity> customers) {
        this.customers = customers;
    }

    public Set<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    public Set<TransactionEntity> getHistory() {
        return history;
    }

    public void setHistory(Set<TransactionEntity> history) {
        this.history = history;
    }
}