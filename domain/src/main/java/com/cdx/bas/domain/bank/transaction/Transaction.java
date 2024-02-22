package com.cdx.bas.domain.bank.transaction;

import com.cdx.bas.domain.bank.transaction.type.TransactionType;
import com.cdx.bas.domain.bank.transaction.validation.*;
import com.cdx.bas.domain.currency.validation.ValidCurrency;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import com.cdx.bas.domain.transaction.validation.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
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

    @Min(value = 1, message = "Id must be positive and greater than 0 for existing transaction.", groups = ExistingTransaction.class)
    @NotNull(message = "Id must not be null for existing transaction.", groups = ExistingTransaction.class)
    @Null(message = "Id must be null for new transaction.", groups = NewTransaction.class)
    private Long id;

    @Null(message = "Sender account must be null for cash movement.", groups = CashMovement.class)
    @NotNull(message = "Sender account id must not be null.", groups = AccountMovement.class)
    private Long senderAccountId;

    @NotNull(message = "receiver account id must not be null.")
    private Long receiverAccountId;

    @Min(value = 10, message = "Amount must be greater than 10 for cash movement.", groups = CashMovement.class)
    @Min(value = 1, message = "Amount must be positive and greater than 0.", groups = AccountMovement.class)
    @NotNull(message = "Amount must not be null.")
    private BigDecimal amount;

    @ValidCurrency
    @NotNull(message = "Currency must not be null.")
    private String currency;

    @NotNull(message = "Type must not be null.")
    private TransactionType type;

    @ValidStatus(expectedStatus = UNPROCESSED, groups = NewTransaction.class)
    @NotNull(message = "Status must not be null.")
    private TransactionStatus status;

    @NotNull(message = "Date must not be null.")
    private Instant date;

    @NotNull(message = "Label must not be null.")
    private String label;

    @NotNull(message = "Bill must be define for cash movements.", groups = CashMovement.class)
    private Map<String, String> metadata = new HashMap<>();

    @Override
    public int compareTo(Transaction transactionToCompare) {
        return this.getDate().compareTo(transactionToCompare.getDate());
    }
}
