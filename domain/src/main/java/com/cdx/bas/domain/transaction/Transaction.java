package com.cdx.bas.domain.transaction;

import com.cdx.bas.domain.validator.ValidCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class Transaction implements Comparable<Transaction> {

    @NotNull(message = "id must not be null.")
    @Min(value = 1, message = "id must be positive and greater than 0.")
    private Long id;

    @NotNull(message = "accountId must not be null.")
    private Long senderAccountId;

    @NotNull(message = "accountId must not be null.")
    private Long receiverAccountId;

    @Min(value = 1, message = "amount must be positive and greater than 0.")
    private BigDecimal amount;

    @NotNull(message = "currency must not be null.")
    @ValidCurrency
    private String currency;

    @NotNull(message = "type must not be null.")
    private TransactionType type;

    @NotNull(message = "status must not be null.")
    private TransactionStatus status;

    @NotNull(message = "date must not be null.")
    private Instant date;

    @NotNull(message = "label must not be null.")
    private String label;

    private Map<String, String> metadata = new HashMap<>();

    @Override
    public int compareTo(Transaction transactionToCompare) {
        return this.getDate().compareTo(transactionToCompare.getDate());
    }
}
