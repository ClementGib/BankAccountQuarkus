package com.cdx.bas.domain.bank.transaction;

import java.time.Instant;

import net.dv8tion.jda.api.MessageBuilder;

public record Transaction(long accountId, 
        long amount, 
        TransactionType type, 
        TransactionStatus status, 
        Instant date, 
        String label) {
    
    public Transaction(Transaction transaction, TransactionStatus status) {
        this(transaction.accountId, transaction.amount, transaction.type, status, transaction.date, transaction.label);
    }
    
    public void validate() {
        MessageBuilder messageBuilder = new MessageBuilder();
        
        if (this.type() == null) {
            messageBuilder.append("Transaction type must not be null.\n");
        } else if (this.type() == TransactionType.CREDIT && this.amount() <= 0) {
            messageBuilder.append("Credit transaction amount must be greater than 0.\n");
        } else if (this.type() == TransactionType.DEBIT && this.amount() <= 0) {
            messageBuilder.append("Debit transaction amount must be greater than 0.\n");
        }
        
        if (this.type() == null) {
            messageBuilder.append("Transaction status must not be null.\n");
        }
        
        if (this.date() == null) {
            messageBuilder.append("Transaction date must not be null.\n");
        }
        
        if (this.label() == null) {
            messageBuilder.append("Transaction label must not be null.\n");
        }
        
        if (messageBuilder.length() > 0) {
            throw new IllegalStateException(messageBuilder.build().getContentRaw());
        }
    }
    
}
