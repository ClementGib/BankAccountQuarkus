package com.cdx.bas.domain.bank.transaction;

import java.time.Instant;

public record Transaction(long amount, TransactionType type, Instant date, String label) {
    
}
