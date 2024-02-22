package com.cdx.bas.application.bank.transaction;

import com.cdx.bas.application.bank.account.BankAccountTestUtils;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.type.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.ERROR;
import static com.cdx.bas.domain.bank.transaction.type.TransactionType.CREDIT;

public class TransactionTestUtils {
    public static Transaction createTransactionUtils(long senderAccountId, long receiverAccountId,
                                                      BigDecimal amount, TransactionStatus status,
                                                      Instant date, Map<String, String> metadata) {
        return Transaction.builder()
                .id(1L)
                .amount(amount)
                .currency("EUR")
                .senderAccountId(senderAccountId)
                .receiverAccountId(receiverAccountId)
                .type(TransactionType.CREDIT)
                .status(status)
                .date(date)
                .label("transaction of " + amount)
                .metadata(metadata).build();
    }

    public static Transaction createTransactionUtils(long id, long amount, Instant date, String label) {
        return Transaction.builder()
                .id(id)
                .amount(new BigDecimal(amount))
                .senderAccountId(99L)
                .receiverAccountId(77L)
                .type(TransactionType.CREDIT)
                .status(TransactionStatus.UNPROCESSED)
                .date(date)
                .label(label)
                .build();
    }

    public static Transaction createTransactionUtils(long senderAccountId, long receiverAccountId, Instant instantDate) {
        Map<String, String> metadata = Map.of("amount_before", "0", "amount_after", "100");
        return Transaction.builder()
                .id(10L)
                .senderAccountId(senderAccountId)
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

    public static Transaction createTransaction(long id, long senderAccountId, Instant instantDate) {
        return Transaction.builder()
                .id(id)
                .type(CREDIT)
                .senderAccountId(senderAccountId)
                .receiverAccountId(77L)
                .amount(new BigDecimal("100"))
                .status(ERROR)
                .date(instantDate)
                .label("transaction test")
                .build();
    }

    public static Transaction createTransactionUtils(long accountId, Instant instantDate) {
        Map<String, String> metadata = Map.of("amount_before", "0", "amount_after", "350");
        return Transaction.builder()
                .id(2L)
                .senderAccountId(accountId)
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
        transactionEntity.setSenderBankAccountEntity(null);
        transactionEntity.setReceiverBankAccountEntity(null);
        transactionEntity.setAmount(new BigDecimal("100"));
        transactionEntity.setType(TransactionType.CREDIT);
        transactionEntity.setStatus(TransactionStatus.ERROR);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        return transactionEntity;
    }

    public static TransactionEntity createTransactionEntityUtils(long senderAccountId, long receiverAccountId, Instant instantDate) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(10L);
        transactionEntity.setSenderBankAccountEntity(BankAccountTestUtils.createBankAccountEntity(senderAccountId));
        transactionEntity.setReceiverBankAccountEntity(BankAccountTestUtils.createBankAccountEntity(receiverAccountId));
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
