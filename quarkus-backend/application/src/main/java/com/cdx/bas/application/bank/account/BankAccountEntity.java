package com.cdx.bas.application.bank.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.cdx.bas.application.customer.CustomerEntity;
import com.cdx.bas.application.transaction.TransactionEntity;
import com.cdx.bas.domain.bank.account.AccountType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(schema = "basapp", name = "bank_accounts", uniqueConstraints = @UniqueConstraint(columnNames = "account_id"))
public class BankAccountEntity extends PanacheEntityBase {

    @Id
    @Column(name = "account_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_accounts_account_id_seq_gen")
    @SequenceGenerator(name = "bank_accounts_account_id_seq_gen", sequenceName = "bank_accounts_account_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;
    
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType type;
    
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    
    @ManyToMany(mappedBy = "accounts", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<CustomerEntity> customers = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinTable(name = "bank_accounts_transactions", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "transaction_id"))
    @OrderBy("date")
    private Set<TransactionEntity> transactions = new HashSet<>();

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

    public List<CustomerEntity> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerEntity> customers) {
        this.customers = customers;
    }

    public Set<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionEntity> transactions) {
        this.transactions = transactions;
    }
}