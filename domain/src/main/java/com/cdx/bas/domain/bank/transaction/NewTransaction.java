package com.cdx.bas.domain.bank.transaction;

import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewTransaction {

    private Long emitterAccountId;

    private Long receiverAccountId;

    private BigDecimal amount;

    private String currency;

    private TransactionType type;

    private String label;

    private Map<String, String> metadata = new HashMap<>();
}
