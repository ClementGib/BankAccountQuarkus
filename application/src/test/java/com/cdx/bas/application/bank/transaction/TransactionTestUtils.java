package com.cdx.bas.application.bank.transaction;

import com.cdx.bas.application.bank.account.BankAccountEntityUtils;
import com.cdx.bas.domain.bank.transaction.NewTransaction;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.type.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.ERROR;
import static com.cdx.bas.domain.bank.transaction.type.TransactionType.CREDIT;

public class TransactionTestUtils {
    public static Transaction createTransactionUtils(long emitterAccountId, long receiverAccountId,
                                                      BigDecimal amount, TransactionStatus status,
                                                      Instant date, Map<String, String> metadata) {
        return Transaction.builder()
                .id(1L)
                .amount(amount)
                .currency("EUR")
                .emitterAccountId(emitterAccountId)
                .receiverAccountId(receiverAccountId)
                .type(TransactionType.CREDIT)
                .status(status)
                .date(date)
                .label("transaction of " + amount)
                .metadata(metadata).build();
    }

    public static Transaction createTransactionUtils(Long id, Long amount, Instant date, String label) {
        return Transaction.builder()
                .id(id)
                .amount(new BigDecimal(amount))
                .emitterAccountId(99L)
                .receiverAccountId(77L)
                .type(TransactionType.CREDIT)
                .status(TransactionStatus.UNPROCESSED)
                .date(date)
                .label(label)
                .build();
    }

    public static NewTransaction createNewTransactionUtils(long amount, String label) {
        return NewTransaction.builder()
                .amount(new BigDecimal(amount))
                .emitterAccountId(99L)
                .receiverAccountId(77L)
                .type(TransactionType.CREDIT)
                .label(label)
                .build();
    }

    public static Transaction createTransactionUtils(Long emitterAccountId, Long receiverAccountId, Instant instantDate) {
        Map<String, String> metadata = Map.of("amount_before", "0", "amount_after", "100");
        return Transaction.builder()
                .id(10L)
                .emitterAccountId(emitterAccountId)
                .receiverAccountId(receiverAccountId)
                .amount(new BigDecimal(100))
                .currency("EUR")
                .type(TransactionType.CREDIT)
                .status(TransactionStatus.COMPLETED)
                .date(instantDate)
                .label("transaction test")
                .metadata(metadata)
                .build();
    }

    public static Transaction createTransaction(Long id, Long emitterBankAccount, Instant instantDate) {
        return Transaction.builder()
                .id(id)
                .type(CREDIT)
                .emitterAccountId(emitterBankAccount)
                .receiverAccountId(77L)
                .amount(new BigDecimal("100"))
                .currency("EUR")
                .status(ERROR)
                .date(instantDate)
                .label("transaction test")
                .build();
    }

    public static Transaction createTransactionUtils(long accountId, Instant instantDate) {
        Map<String, String> metadata = Map.of("amount_before", "0", "amount_after", "350");
        return Transaction.builder()
                .id(2L)
                .emitterAccountId(accountId)
                .receiverAccountId(77L)
                .amount(new BigDecimal(100))
                .type(TransactionType.CREDIT)
                .status(TransactionStatus.ERROR)
                .date(instantDate)
                .label("transaction test")
                .metadata(metadata)
                .build();
    }

    public static TransactionEntity createTransactionEntity(long id, Instant instantDate) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(id);
        transactionEntity.setEmitterBankAccountEntity(null);
        transactionEntity.setReceiverBankAccountEntity(null);
        transactionEntity.setAmount(new BigDecimal("100"));
        transactionEntity.setType(TransactionType.CREDIT);
        transactionEntity.setStatus(TransactionStatus.ERROR);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        return transactionEntity;
    }

    public static TransactionEntity createTransactionEntityUtils(long emitterAccountId, long receiverAccountId, Instant instantDate) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(10L);
        transactionEntity.setEmitterBankAccountEntity(BankAccountEntityUtils.createBankAccountEntity(emitterAccountId));
        transactionEntity.setReceiverBankAccountEntity(BankAccountEntityUtils.createBankAccountEntity(receiverAccountId));
        transactionEntity.setAmount(new BigDecimal("100"));
        transactionEntity.setCurrency("EUR");
        transactionEntity.setType(TransactionType.CREDIT);
        transactionEntity.setStatus(TransactionStatus.COMPLETED);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        transactionEntity.setMetadata("{\"amount_after\" : \"100\", \"amount_before\" : \"0\"}");
        return transactionEntity;
    }
}
