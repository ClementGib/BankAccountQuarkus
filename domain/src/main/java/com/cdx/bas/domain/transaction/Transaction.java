package com.cdx.bas.domain.transaction;

import com.cdx.bas.domain.currency.ValidCurrency;
import com.cdx.bas.domain.transaction.validation.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.cdx.bas.domain.transaction.TransactionStatus.UNPROCESSED;

@Data
@Builder
@AllArgsConstructor
public class Transaction implements Comparable<Transaction> {

    @Min(value = 1, message = "id must be positive and greater than 0 for existing transaction.", groups = ExistingTransaction.class)
    @NotNull(message = "id must not be null for existing transaction.", groups = ExistingTransaction.class)
    @Null(message = "id must be null for new transaction.", groups = NewTransaction.class)
    private Long id;

    @Null(message = "sender account must be null for cash movement.", groups = CashMovement.class)
    @NotNull(message = "sender account id must not be null.", groups = AccountMovement.class)
    private Long senderAccountId;

    @NotNull(message = "receiver account id must not be null.")
    private Long receiverAccountId;

    @Min(value = 10, message = "amount must be greater than 10 for cash movement.", groups = CashMovement.class)
    @Min(value = 1, message = "amount must be positive and greater than 0.", groups = AccountMovement.class)
    @NotNull(message = "amount must not be null.")
    private BigDecimal amount;

    @ValidCurrency
    @NotNull(message = "currency must not be null.")
    private String currency;

    @NotNull(message = "type must not be null.")
    private TransactionType type;

    @ValidStatus(expectedStatus = UNPROCESSED, groups = NewTransaction.class)
    @NotNull(message = "status must not be null.")
    private TransactionStatus status;

    @NotNull(message = "date must not be null.")
    private Instant date;

    @NotNull(message = "label must not be null.")
    private String label;

    @NotNull(message = "bill must be define for cash movements.", groups = CashMovement.class)
    private Map<String, String> metadata = new HashMap<>();

    @Override
    public int compareTo(Transaction transactionToCompare) {
        return this.getDate().compareTo(transactionToCompare.getDate());
    }
}
