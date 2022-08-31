package com.cdx.bas.domain.bank.transaction;

import java.time.Instant;
import java.util.Objects;

public record Transaction(long accountId, long amount, TransactionType type, Instant date, String label) {
    
}
