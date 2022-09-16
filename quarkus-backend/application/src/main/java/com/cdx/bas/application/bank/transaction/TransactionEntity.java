package com.cdx.bas.application.bank.transaction;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(schema = "basapp", name = "transactions", uniqueConstraints = @UniqueConstraint(columnNames = "transaction_id"))
public class TransactionEntity {
    
    @Id
    @Column(name = "transaction_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_transaction_id_seq_gen")
    @SequenceGenerator(name = "transactions_transaction_id_seq_gen", sequenceName = "customers_customer_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;
    
    @Column(name = "type", nullable = false)
    private String type;
    
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "date", nullable = false)
    private Instant date;
    
    @Column(name = "label", nullable = false)
    private String label;
}
