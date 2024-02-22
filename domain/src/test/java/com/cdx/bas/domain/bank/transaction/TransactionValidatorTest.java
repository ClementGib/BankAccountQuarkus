package com.cdx.bas.domain.bank.transaction;

import com.cdx.bas.domain.bank.transaction.validation.TransactionValidator;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.ERROR;
import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.UNPROCESSED;
import static com.cdx.bas.domain.bank.transaction.type.TransactionType.*;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class TransactionValidatorTest {

    @Inject
    TransactionValidator transactionValidator;

    @Test
    public void validateNewTransaction_shouldDoNothing_whenNewCreditTransactionIsValid(){
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .senderAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        transactionValidator.validateNewTransaction(creditTransaction);
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenNewCreditTransactionIsEmpty(){
        Transaction creditTransaction = Transaction.builder()
                .build();
        try {
            transactionValidator.validateNewTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class);
            String[] lines = transactionException.getMessage().split("\\r?\\n");
            assertThat(lines).hasSize(10);
        }
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenNewTransactionHasId(){
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .senderAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
       try {
           transactionValidator.validateNewTransaction(creditTransaction);
       } catch (TransactionException transactionException) {
           assertThat(transactionException).isInstanceOf(TransactionException.class)
                   .hasMessage("Id must be null for new transaction.\n");
       }
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenNewCreditTransactionAmountIsLowerThanMin(){
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .senderAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("0"))
                .currency("EUR")
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        try {
            transactionValidator.validateNewTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be positive and greater than 0.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldDoNothing_whenNewDebitTransactionIsValid(){
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .senderAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(DEBIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        transactionValidator.validateNewTransaction(creditTransaction);
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenNewDebitTransactionHasId(){
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .senderAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(DEBIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        try {
            transactionValidator.validateNewTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Id must be null for new transaction.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenNewDebitTransactionAmountIsLowerThanMin(){
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .senderAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("0"))
                .currency("EUR")
                .type(DEBIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        try {
            transactionValidator.validateNewTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be positive and greater than 0.\n");
        }
    }

    @Test
    public void validateExistingTransaction_shouldDoNothing_whenExistingCreditTransactionIsValid(){
        Transaction creditTransaction = Transaction.builder()
                .id(100L)
                .senderAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        transactionValidator.validateExistingTransaction(creditTransaction);
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenExistingTransactionIsEmpty(){
        Transaction creditTransaction = Transaction.builder()
                .build();
        try {
            transactionValidator.validateExistingTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class);
            String[] lines = transactionException.getMessage().split("\\r?\\n");
            assertThat(lines).hasSize(10);
        }
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenExistingCreditTransactionAmountIsLowerThanMin(){
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .senderAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("0"))
                .currency("EUR")
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        try {
            transactionValidator.validateExistingTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be positive and greater than 0.\n");
        }
    }

    @Test
    public void validateExistingTransaction_shouldDoNothing_whenExistingDebitTransactionIsValid(){
        Transaction creditTransaction = Transaction.builder()
                .id(100L)
                .senderAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(DEBIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        transactionValidator.validateExistingTransaction(creditTransaction);
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenExistingDebitTransactionAmountIsLowerThanMin(){
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .senderAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("0"))
                .currency("EUR")
                .type(DEBIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        try {
            transactionValidator.validateExistingTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be positive and greater than 0.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldDoNothing_whenNewDepositTransactionIsValid(){
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .senderAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("20"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(Map.of("bill", "10,10"))
                .build();
        transactionValidator.validateNewTransaction(creditTransaction);
    }

    @Test
    public void validateNewTransaction_shouldThrowTransactionException_whenNewDepositTransactionHasSendAccountId(){
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .senderAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("20"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(Map.of("bill", "10,10"))
                .build();
        try {
            transactionValidator.validateExistingTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Sender account must be null for cash movement.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldThrowTransactionException_whenNewDepositTransactionHasAmountLowerThanMin(){
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .senderAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("5"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(Map.of("bill", "5"))
                .build();
        try {
            transactionValidator.validateNewTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be greater than 10 for cash movement.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldThrowTransactionException_whenNewDepositTransactionHasEmptyMetadata(){
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .senderAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("10"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(new HashMap<>())
                .build();
        try {
            transactionValidator.validateNewTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Bill must be define for cash movements.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldThrowTransactionException_whenNewDepositTransactionHasWrongStatus(){
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .senderAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("10"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(ERROR)
                .date(Instant.now())
                .label("new transaction")
                .metadata(Map.of("bill", "10"))
                .build();
        try {
            transactionValidator.validateNewTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Unexpected transaction status.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldThrowTransactionException_whenNewDepositTransactionHasNullMetadata(){
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .senderAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("10"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(null)
                .build();
        try {
            transactionValidator.validateNewTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Bill must be define for cash movements.\n");
        }
    }

    @Test
    public void validateExistingTransaction_shouldDoNothing_whenExistingDepositTransactionIsValid(){
        Transaction creditTransaction = Transaction.builder()
                .id(100L)
                .senderAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("20"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(Map.of("bill", "10,10"))
                .build();
        transactionValidator.validateExistingTransaction(creditTransaction);
    }

    @Test
    public void validateExistingTransaction_shouldThrowTransactionException_whenExistingDepositTransactionHasAmountLowerThanMin(){
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .senderAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("5"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(Map.of("bill", "5"))
                .build();
        try {
            transactionValidator.validateExistingTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be greater than 10 for cash movement.\n");
        }
    }

    @Test
    public void validateExistingTransaction_shouldThrowTransactionException_whenExistingDepositTransactionHasEmptyMetadata(){
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .senderAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("10"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(new HashMap<>())
                .build();
        try {
            transactionValidator.validateExistingTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Bill must be define for cash movements.\n");
        }
    }

    @Test
    public void validateExistingTransaction_shouldThrowTransactionException_whenExistingDepositTransactionHasNullMetadata(){
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .senderAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("10"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(null)
                .build();
        try {
            transactionValidator.validateExistingTransaction(creditTransaction);
        } catch (TransactionException transactionException) {
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Bill must be define for cash movements.\n");
        }
    }
}