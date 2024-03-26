package com.cdx.bas.application.bank.transaction;

import com.cdx.bas.domain.bank.transaction.NewTransaction;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionException;
import com.cdx.bas.domain.bank.transaction.TransactionPersistencePort;
import com.cdx.bas.domain.bank.transaction.type.TransactionTypeProcessingServicePort;
import com.cdx.bas.domain.bank.transaction.validation.TransactionValidator;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.COMPLETED;
import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.UNPROCESSED;
import static com.cdx.bas.domain.bank.transaction.type.TransactionType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class TransactionServiceImplTest {

    Clock clock = Mockito.mock(Clock.class);
    TransactionPersistencePort transactionRepository = Mockito.mock(TransactionPersistencePort.class);
    TransactionTypeProcessingServicePort transactionTypeProcessingServicePort = Mockito.mock(TransactionTypeProcessingServicePort.class);
    TransactionValidator transactionValidator = Mockito.mock(TransactionValidator.class);

	TransactionServiceImpl transactionService = new TransactionServiceImpl(transactionRepository, transactionValidator, transactionTypeProcessingServicePort, clock);


    @Test
    public void shouldReturnAllTransactions_whenRepositoryReturnsTransactions() {
        // Arrange
        Set<Transaction> transactions = Set.of(
                Transaction.builder().id(1L).build(),
                Transaction.builder().id(2L).build(),
                Transaction.builder().id(3L).build()
        );
        when(transactionRepository.getAll()).thenReturn(transactions);

        // Act
        Set<Transaction> actual = transactionService.getAll();

        // Assert
        assertThat(actual).isEqualTo(transactions);
        verify(transactionRepository).getAll();
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    public void shouldReturnTransactionCorrespondingToStatus_whenStatusIsValid() {
        // Arrange
        Set<Transaction> transactions = Set.of(
                Transaction.builder().id(1L).status(COMPLETED).build(),
                Transaction.builder().id(2L).status(COMPLETED).build(),
                Transaction.builder().id(3L).status(COMPLETED).build()
        );
        when(transactionRepository.findAllByStatus(COMPLETED)).thenReturn(transactions);

        // Act
        Set<Transaction> actual = transactionService.findAllByStatus("COMPLETED");

        // Assert
        assertThat(actual).isEqualTo(transactions);
        verify(transactionRepository).findAllByStatus(COMPLETED);
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    public void shouldThrowException_whenStatusIsInvalid() {
        // Act
        try {
            transactionService.findAllByStatus("INVALID");
        } catch (IllegalArgumentException exception) {
            // Assert
            assertThat(exception.getMessage()).isEqualTo("Invalid status: INVALID");
        }
        verifyNoInteractions(transactionRepository);
    }

    @Test
    public void shouldCreateTransaction_whenNewTransactionIsValid() {
        // Arrange
        Instant timestamp = Instant.parse("2024-03-14T12:00:00Z");
        NewTransaction newTransaction = new NewTransaction(99L, 77L,
                new BigDecimal("100"), "EUR",
                CREDIT, "transaction test", new HashMap<>());
        Transaction transactionToCreate = Transaction.builder()
                .id(null)
                .emitterAccountId(99L)
                .receiverAccountId(77L)
                .amount(new BigDecimal("100"))
                .currency("EUR")
                .label("transaction test")
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(timestamp)
                .metadata(new HashMap<>())
                .build();
        when(clock.instant()).thenReturn(timestamp);

        // Act
        transactionService.createDigitalTransaction(newTransaction);

        // Assert
        verify(transactionValidator).validateNewDigitalTransaction(transactionToCreate);
        verify(transactionRepository).create(transactionToCreate);
        verifyNoMoreInteractions(transactionValidator, transactionRepository);
    }

    @Test
    public void shouldThrowException_whenNewTransactionIsInvalid() {
        // Arrange
        Instant timestamp = Instant.parse("2024-03-14T12:00:00Z");
        Transaction invalidTransaction = new Transaction();
        invalidTransaction.setDate(timestamp);
        invalidTransaction.setStatus(UNPROCESSED);
        invalidTransaction.setMetadata(null);

        when(clock.instant()).thenReturn(timestamp);
        doThrow(new TransactionException("invalid transaction...")).when(transactionValidator).validateNewDigitalTransaction(invalidTransaction);

        try {
            // Act
            transactionService.createDigitalTransaction(new NewTransaction(null, null, null, null, null, null, null));
        } catch (TransactionException exception) {
            // Assert
            assertThat(exception.getMessage()).isEqualTo("invalid transaction...");
        }
        verify(transactionValidator).validateNewDigitalTransaction(invalidTransaction);
        verifyNoMoreInteractions(transactionValidator);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    public void shouldMergeOldTransactionWithNewTransaction_whenOldTransactionAndNewTransactionAreValid() {
        // Arrange
        Transaction oldTransaction = Transaction.builder()
                .id(1L)
                .amount(new BigDecimal(100))
                .emitterAccountId(10L)
                .receiverAccountId(11L)
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("old transaction")
                .build();

        Instant dateAfter = Instant.now();
        BigDecimal bigDecimalAfter = new BigDecimal(200);
        String labelAfter = "new transaction";
        Transaction newTransaction = Transaction.builder()
                .id(2L)
                .amount(bigDecimalAfter)
                .emitterAccountId(20L)
                .receiverAccountId(22L)
                .type(DEBIT)
                .status(UNPROCESSED)
                .date(dateAfter)
                .label(labelAfter)
                .build();

        // Act
        Transaction actualTransaction = transactionService.mergeTransactions(oldTransaction, newTransaction);

        oldTransaction.setId(2L);
        oldTransaction.setAmount(bigDecimalAfter);
        oldTransaction.setEmitterAccountId(20L);
        oldTransaction.setReceiverAccountId(22L);
        oldTransaction.setType(DEBIT);
        oldTransaction.setStatus(UNPROCESSED);
        oldTransaction.setDate(dateAfter);
        oldTransaction.setLabel(labelAfter);

        // Assert
        assertThat(actualTransaction).isEqualTo(oldTransaction);
    }

    @Test
    public void shouldFindTransaction_whenTransactionExists() {
        // Arrange
        Transaction transaction = Transaction.builder()
                .id(1L)
                .amount(new BigDecimal(100))
                .emitterAccountId(100L)
                .receiverAccountId(200L)
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("deposit of 100 euros")
                .build();
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        // Act
        Transaction actualTransaction = transactionService.findTransaction(1L);

        // Assert
        assertThat(actualTransaction).isEqualTo(transaction);
        verify(transactionRepository).findById(1L);
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    public void shouldReturnNull_whenTransactionDoesNotExist() {
        // Arrange
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Transaction actualTransaction = transactionService.findTransaction(1L);

        // Assert
        assertThat(actualTransaction).isNull();
        verify(transactionRepository).findById(1L);
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    public void shouldProcessBankAccountCredit_whenTransactionHasCreditType() {
        // Arrange
        Transaction transaction = Transaction.builder()
                .id(1L)
                .amount(new BigDecimal(100))
                .emitterAccountId(100L)
                .receiverAccountId(200L)
                .type(CREDIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("deposit of 100 euros")
                .build();

        // Act
        transactionService.process(transaction);

        // Assert
        verify(transactionTypeProcessingServicePort).credit(transaction);
        verifyNoMoreInteractions(transactionTypeProcessingServicePort);
    }

    @Test
    public void shouldProcessBankAccountDeposit_whenTransactionHasDepositType() {
        // Arrange
        Transaction transaction = Transaction.builder()
                .id(1L)
                .amount(new BigDecimal(100))
                .emitterAccountId(100L)
                .receiverAccountId(200L)
                .type(DEPOSIT)
                .status(UNPROCESSED)
                .date(Instant.now())
                .label("deposit of 100 euros")
                .build();

        // Act
        transactionService.process(transaction);

        // Assert
        verify(transactionTypeProcessingServicePort).deposit(transaction);
        verifyNoMoreInteractions(transactionTypeProcessingServicePort);
    }
}
