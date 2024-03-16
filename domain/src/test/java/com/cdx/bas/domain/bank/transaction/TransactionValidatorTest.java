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
import static org.junit.jupiter.api.Assertions.fail;

@QuarkusTest
class TransactionValidatorTest {

    @Inject
    TransactionValidator transactionValidator;

    @Test
    public void validateNewTransaction_shouldDoNothing_whenNewCreditTransactionIsValid() {
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
        transactionValidator.validateNewTransaction(creditTransaction);
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenNewCreditTransactionIsEmpty() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .build();
        try {
            // Act
            transactionValidator.validateNewTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class);
            String[] lines = transactionException.getMessage().split("\\r?\\n");
            assertThat(lines).hasSize(8);
        }
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenNewTransactionHasId() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .emitterAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        try {
            // Act
            transactionValidator.validateNewTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Id must be null for new transaction.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenNewCreditTransactionAmountIsLowerThanMin() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(null)
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
            transactionValidator.validateNewTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be positive and greater than 0.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldDoNothing_whenNewDebitTransactionIsValid() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(null)
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
        transactionValidator.validateNewTransaction(creditTransaction);
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenNewDebitTransactionHasId() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
                .emitterAccountId(1L)
                .receiverAccountId(2L)
                .amount(new BigDecimal("1"))
                .currency("EUR")
                .type(DEBIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("new transaction")
                .build();
        try {
            // Act
            transactionValidator.validateNewTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Id must be null for new transaction.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenNewDebitTransactionAmountIsLowerThanMin() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(null)
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
            transactionValidator.validateNewTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be positive and greater than 0.\n");
        }
    }

    @Test
    public void validateExistingTransaction_shouldDoNothing_whenExistingCreditTransactionIsValid() {
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
        transactionValidator.validateExistingTransaction(creditTransaction);
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenExistingTransactionIsEmpty() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .build();
        try {
            // Act
            transactionValidator.validateExistingTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class);
            String[] lines = transactionException.getMessage().split("\\r?\\n");
            assertThat(lines).hasSize(9);
        }
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenExistingCreditTransactionAmountIsLowerThanMin() {
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
            transactionValidator.validateExistingTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be positive and greater than 0.\n");
        }
    }

    @Test
    public void validateExistingTransaction_shouldDoNothing_whenExistingDebitTransactionIsValid() {
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
        transactionValidator.validateExistingTransaction(creditTransaction);
    }

    @Test
    public void validateNewTransaction_shouldTrowTransactionException_whenExistingDebitTransactionAmountIsLowerThanMin() {
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
            transactionValidator.validateExistingTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be positive and greater than 0.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldDoNothing_whenNewDepositTransactionIsValid() {
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
        transactionValidator.validateNewTransaction(creditTransaction);
    }

    @Test
    public void validateNewTransaction_shouldThrowTransactionException_whenNewDepositTransactionHasSendAccountId() {
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
            transactionValidator.validateNewTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessageContaining("Emitter account id must be null for cash movement.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldThrowTransactionException_whenNewDepositTransactionHasAmountLowerThanMin() {
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
            transactionValidator.validateNewTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be greater than 10 for cash movement.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldThrowTransactionException_whenNewDepositTransactionHasEmptyMetadata() {
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
            transactionValidator.validateNewTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Bill must be define for cash movements.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldThrowTransactionException_whenNewDepositTransactionHasWrongStatus() {
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
            transactionValidator.validateNewTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Unexpected transaction status ERROR, expected status: UNPROCESSED.\n");
        }
    }

    @Test
    public void validateNewTransaction_shouldThrowTransactionException_whenNewDepositTransactionHasNullMetadata() {
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
            transactionValidator.validateNewTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessageContaining("Metadata must not be null for cash movements.\n")
                    .hasMessageContaining("Bill must be define for cash movements.\n");
        }
    }

    @Test
    public void validateExistingTransaction_shouldDoNothing_whenExistingDepositTransactionIsValid() {
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
        transactionValidator.validateExistingTransaction(creditTransaction);
    }

    @Test
    public void validateExistingTransaction_shouldThrowTransactionException_whenExistingDepositTransactionHasAmountLowerThanMin() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
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
            transactionValidator.validateExistingTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Amount must be greater than 10 for cash movement.\n");
        }
    }

    @Test
    public void validateExistingTransaction_shouldThrowTransactionException_whenExistingDepositTransactionHasEmptyMetadata() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
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
            transactionValidator.validateExistingTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessage("Bill must be define for cash movements.\n");
        }
    }

    @Test
    public void validateExistingTransaction_shouldThrowTransactionException_whenExistingDepositTransactionHasNullMetadata() {
        // Arrange
        Transaction creditTransaction = Transaction.builder()
                .id(99L)
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
            transactionValidator.validateExistingTransaction(creditTransaction);
            fail();
        } catch (TransactionException transactionException) {
            // Assert
            assertThat(transactionException).isInstanceOf(TransactionException.class)
                    .hasMessageContaining("Metadata must not be null for cash movements.\n")
                    .hasMessageContaining("Bill must be define for cash movements.\n");
        }
    }
}