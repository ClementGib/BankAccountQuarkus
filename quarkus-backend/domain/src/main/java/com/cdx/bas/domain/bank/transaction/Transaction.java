package com.cdx.bas.domain.bank.transaction;

import java.time.Instant;
import java.util.Objects;

public record Transaction(long accountId, long amount, TransactionType type, Instant date, String label) implements Comparable<Transaction> {

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        return Objects.equals(accountId, other.accountId) 
                && Objects.equals(amount, other.amount)
                && Objects.equals(date, other.date)
                && Objects.equals(label, other.label) 
                && type == other.type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Transaction: ");
        builder.append(accountId);
        builder.append(" - ");
        builder.append(amount);
        builder.append(" - ");
        builder.append(type);
        builder.append(" - ");
        builder.append(date);
        builder.append(" - ");
        builder.append(label);
        return builder.toString();
    }
    @Override
    public int compareTo(Transaction transactionToCompar) {
        return this.date().compareTo(transactionToCompar.date());
    }
}
