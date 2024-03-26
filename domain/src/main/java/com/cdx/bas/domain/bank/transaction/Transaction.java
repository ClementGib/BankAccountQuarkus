package com.cdx.bas.domain.bank.transaction;

import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.type.TransactionType;
import com.cdx.bas.domain.bank.transaction.validation.*;
import com.cdx.bas.domain.currency.validation.ValidCurrency;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.UNPROCESSED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Comparable<Transaction> {

    @Positive(message = "Id must be positive")
    @Min(value = 1, message = "Id must be positive and greater than 0 for existing transaction.", groups = ExistingTransactionGroup.class)
    @NotNull(message = "Id must not be null for existing transaction.", groups = ExistingTransactionGroup.class)
    @Null(message = "Id must be null for new transaction.", groups = NewTransactionGroup.class)
    private Long id;

    @Positive(message = "Emitter account id  must be positive")
    @Null(message = "Emitter account id must be null for cash movement.", groups = CashTransactionGroup.class)
    @NotNull(message = "Emitter account id must not be null.", groups = DigitalTransactionGroup.class)
    private Long emitterAccountId;

    @Positive(message = "Receiver account id  must be positive")
    @NotNull(message = "Receiver account id must not be null.")
    private Long receiverAccountId;

    @Min(value = 10, message = "Amount must be greater than 10 for cash movement.", groups = CashTransactionGroup.class)
    @Min(value = 1, message = "Amount must be positive and greater than 0.", groups = DigitalTransactionGroup.class)
    @NotNull(message = "Amount must not be null.")
    private BigDecimal amount;

    @ValidCurrency
    @NotNull(message = "Currency must not be null.")
    private String currency;

    @NotNull(message = "Type must not be null.")
    @ValidTypes({
            @ValidType(expectedTypes = {TransactionType.CREDIT, TransactionType.DEBIT}, groups = DigitalTransactionGroup.class),
            @ValidType(expectedTypes = {TransactionType.DEPOSIT, TransactionType.WITHDRAW}, groups = CashTransactionGroup.class)
    })
    private TransactionType type;

    @ValidStatus(expectedStatus = UNPROCESSED, groups = NewTransactionGroup.class)
    @NotNull(message = "Status must not be null.")
    private TransactionStatus status;

    @NotNull(message = "Date must not be null.")
    private Instant date;

    @NotNull(message = "Label must not be null.")
    private String label;

    @NotNull(message = "Metadata must not be null for cash movements.", groups = CashTransactionGroup.class)
    @NotEmpty(message = "Bill must be define for cash movements.", groups = CashTransactionGroup.class)
    private Map<String, String> metadata = new HashMap<>();

    @Override
    public int compareTo(Transaction transactionToCompare) {
        return this.getDate().compareTo(transactionToCompare.getDate());
    }
}
