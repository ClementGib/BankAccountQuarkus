package com.cdx.bas.domain.bank.transaction;

import com.cdx.bas.domain.bank.transaction.validation.TransactionValidator;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.ERROR;
import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.UNPROCESSED;
import static com.cdx.bas.domain.bank.transaction.type.TransactionType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@QuarkusTest
class TransactionValidatorTest {

    @Inject
    TransactionValidator transactionValidator;

    @Test
    public void shouldDoNothing_whenNewDigitalTransactionIsValid() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .emitterAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();

        // Act
        transactionValidator.validateNewDigitalTransaction(creditTransaction);
    }

    @Test
    public void shouldThrowTransactionExceptionWithMissingViolation_whenNewDigitalIsEmpty() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .build();
        try {
            // Act
            transactionValidator.validateNewDigitalTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            List<String> expectedErrors = List.of("Id must be null for new transaction.",
                    "Emitter account id must not be null.",
                    "Receiver account id must not be null.",
                    "Amount must not be null.",
                    "Currency must not be null.",
                    "Type must not be null.",
                    "Status must not be null.",
                    "Date must not be null.",
                    "Label must not be null.");
            assertThat(transactionException).isInstanceOf(TransactionException.class);
            List<String> actualErrors = Arrays.stream(transactionException.getMessage().split("\\r?\\n")).toList();
            assertThat(actualErrors)
                    .containsExactlyInAnyOrder()
                    .isEqualTo(expectedErrors);
        }
    }

    @Test
    public void shouldThrowTransactionExceptionWithWrongValue_whenNewDigitalIsHasWrongValue() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(1L)
                .emitterAccountId(-1L)
                .receiverAccountId(-1L)
                .amount(new BigDecimal("0"))
                .currency("NOPE")
                .type(DEPOSIT)
                .status(ERROR)
                .build();
        try {
            // Act
            transactionValidator.validateNewDigitalTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class);
            String[] lines = transactionException.getMessage().split("\\r?\\n");
            assertThat(lines).hasSize(9);
        }
    }

    @Test
    public void shouldDoNothing_whenExistingDigitalTransactionIsValid() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(100L)
                .emitterAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        // Act
        transactionValidator.validateExistingDigitalTransaction(creditTransaction);
    }

    @Test
    public void shouldThrowTransactionException_whenExistingTransactionIsEmpty() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .build();
        try {
            // Act
            transactionValidator.validateExistingDigitalTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class);
            String[] lines = transactionException.getMessage().split("\\r?\\n");
            assertThat(lines).hasSize(10);
        }
    }

    @Test
    public void shouldThrowTransactionException_whenExistingCreditTransactionAmountIsLowerThanMin() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .emitterAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("0"))
                .currency("EUR")
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        try {
            // Act
            transactionValidator.validateExistingDigitalTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be positive and greater than 0.\n");
        }
    }

    @Test
    public void shouldDoNothing_whenExistingDebitTransactionIsValid() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(100L)
                .emitterAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(DEBIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        // Act
        transactionValidator.validateNewDigitalTransaction(creditTransaction);
    }

    @Test
    public void shouldThrowTransactionException_whenExistingDebitTransactionAmountIsLowerThanMin() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .emitterAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("0"))
                .currency("EUR")
                .type(DEBIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        try {
            // Act
            transactionValidator.validateNewDigitalTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be positive and greater than 0.\n");
        }
    }

    @Test
    public void shouldDoNothing_whenDepositTransactionIsValid() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .emitterAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("20"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(Map.of("bill", "10,10"))
                .build();
        // Act
        transactionValidator.validateCashTransaction(creditTransaction);
    }

    @Test
    public void shouldThrowTransactionException_whenDepositTransactionHasSendAccountId() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .emitterAccountId(1L)
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
            // Act
            transactionValidator.validateCashTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessageContaining("Emitter account id must be null for cash movement.\n");
        }
    }

    @Test
    public void shouldThrowTransactionException_whenDepositTransactionHasAmountLowerThanMin() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .emitterAccountId(null)
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
            // Act
            transactionValidator.validateCashTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be greater than 10 for cash movement.\n");
        }
    }

    @Test
    public void shouldThrowTransactionException_whenDepositTransactionHasEmptyMetadata() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .emitterAccountId(null)
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
            // Act
            transactionValidator.validateCashTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Bill must be define for cash movements.\n");
        }
    }

    @Test
    public void shouldThrowTransactionException_whenDepositTransactionHasWrongStatus() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .emitterAccountId(null)
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
            // Act
            transactionValidator.validateCashTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Unexpected transaction status ERROR, expected status: UNPROCESSED.\n");
        }
    }

    @Test
    public void shouldThrowTransactionException_whenDepositTransactionHasNullMetadata() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(null)
                .emitterAccountId(null)
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
            // Act
            transactionValidator.validateCashTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessageContaining("Metadata must not be null for cash movements.\n")
                    .hasMessageContaining("Bill must be define for cash movements.\n");
        }
    }

    @Test
    public void shouldDoNothing_whenExistingDepositTransactionIsValid() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(100L)
                .emitterAccountId(null)
                .receiverAccountId(2L)
                .amount(new BigDecimal("20"))
                .currency("EUR")
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .metadata(Map.of("bill", "10,10"))
                .build();
        // Act
        transactionValidator.validateCashTransaction(creditTransaction);
    }
}