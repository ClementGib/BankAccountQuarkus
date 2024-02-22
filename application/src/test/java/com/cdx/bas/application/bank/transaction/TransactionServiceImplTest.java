package com.cdx.bas.application.bank.transaction;

import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionException;
import com.cdx.bas.domain.bank.transaction.TransactionPersistencePort;
import com.cdx.bas.domain.bank.transaction.TransactionServicePort;
import com.cdx.bas.domain.transaction.*;
import com.cdx.bas.domain.bank.transaction.type.TransactionTypeProcessingServicePort;
import com.cdx.bas.domain.bank.transaction.validation.TransactionValidator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.COMPLETED;
import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.UNPROCESSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class TransactionServiceImplTest {
	@InjectMock
	TransactionPersistencePort transactionPersistencePort;

	@InjectMock
    TransactionTypeProcessingServicePort transactionTypeProcessingServicePort;

	@InjectMock
	TransactionValidator transactionValidator;

	@Inject
	TransactionServicePort transactionService;


	@Test
	public void getAll_shouldReturnAllTransactions_whenRepositoryReturnsTransactions() {
		Set<Transaction> transactions = Set.of(
				Transaction.builder().id(1L).build(),
				Transaction.builder().id(2L).build(),
				Transaction.builder().id(3L).build()
		);

		when(transactionPersistencePort.getAll()).thenReturn(transactions);

		Set<Transaction> actual = transactionService.getAll();
		assertThat(actual).isEqualTo(transactions);
		verify(transactionPersistencePort).getAll();
		verifyNoMoreInteractions(transactionPersistencePort);
	}

	@Test
	public void findAllByStatus_shouldReturnTransactionCorrespondingToStatus_whenStatusIsValid() {
		Set<Transaction> transactions = Set.of(
				Transaction.builder().id(1L).status(COMPLETED).build(),
				Transaction.builder().id(2L).status(COMPLETED).build(),
				Transaction.builder().id(3L).status(COMPLETED).build()
		);

		when(transactionPersistencePort.findAllByStatus(COMPLETED)).thenReturn(transactions);

		Set<Transaction> actual = transactionService.findAllByStatus("COMPLETED");
		assertThat(actual).isEqualTo(transactions);
		verify(transactionPersistencePort).findAllByStatus(COMPLETED);
		verifyNoMoreInteractions(transactionPersistencePort);
	}

	@Test
	public void findAllByStatus_shouldThrowException_whenStatusIsInvalid() {
		try {
			transactionService.findAllByStatus("INVALID");
		} catch (IllegalArgumentException exception) {
			assertThat(exception.getMessage()).isEqualTo("Invalid status: INVALID");
		}
		verifyNoInteractions(transactionPersistencePort);
	}

	@Test
	public void createTransaction_shouldCreateTransaction_whenTransactionIsValid() {
		Transaction newTransaction = TransactionTestUtils.createTransactionUtils(1L, 100L, Instant.now(), "transaction test");
		transactionService.createTransaction(newTransaction);
		verify(transactionValidator).validateNewTransaction(newTransaction);
		verify(transactionPersistencePort).create(newTransaction);
		verifyNoMoreInteractions(transactionValidator, transactionPersistencePort);
	}

	@Test
	public void createTransaction_shouldThrowException_whenTransactionIsInvalid() {
		Transaction invalidTransaction = new Transaction();
		doThrow(new TransactionException("invalid transaction...")).when(transactionValidator).validateNewTransaction(invalidTransaction);

		try {
			transactionService.createTransaction(new Transaction());
		} catch (TransactionException exception) {
			assertThat(exception.getMessage()).isEqualTo("invalid transaction...");
		}
		verify(transactionValidator).validateNewTransaction(invalidTransaction);
		verifyNoMoreInteractions(transactionValidator);
		verifyNoInteractions(transactionPersistencePort);
	}

	@Test
	public void mergeTransaction_shouldMergeOldTransactionWithNewTransaction_whenOldTransactionAndNewTransactionAreValid() {
		Transaction oldTransaction = Transaction.builder()
				.id(1L)
				.amount(new BigDecimal(100))
				.senderAccountId(10L)
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
				.senderAccountId(20L)
				.receiverAccountId(22L)
				.type(DEBIT)
				.status(UNPROCESSED)
				.date(dateAfter)
				.label(labelAfter)
				.build();

		Transaction actualTransaction = transactionService.mergeTransactions(oldTransaction, newTransaction);

        oldTransaction.setId(2L);
		oldTransaction.setAmount(bigDecimalAfter);
		oldTransaction.setSenderAccountId(20L);
		oldTransaction.setReceiverAccountId(22L);
		oldTransaction.setType(DEBIT);
		oldTransaction.setStatus(UNPROCESSED);
		oldTransaction.setDate(dateAfter);
		oldTransaction.setLabel(labelAfter);
		assertThat(actualTransaction).isEqualTo(oldTransaction);
	}

	@Test
	public void processTransaction_shouldProcessBankAccountCredit_whenTransactionHasCreditType() {
		Transaction transaction = Transaction.builder()
				.id(1L)
				.amount(new BigDecimal(100))
				.senderAccountId(100L)
				.receiverAccountId(200L)
				.type(CREDIT)
				.status(UNPROCESSED)
				.date(Instant.now())
				.label("deposit of 100 euros")
				.build();

		transactionService.process(transaction);
		verify(transactionTypeProcessingServicePort).credit(transaction);
		verifyNoMoreInteractions(transactionTypeProcessingServicePort);
	}

	@Test
	public void processTransaction_shouldProcessBankAccountDeposit_whenTransactionHasDepositType() {
		Transaction transaction = Transaction.builder()
				.id(1L)
				.amount(new BigDecimal(100))
				.senderAccountId(100L)
				.receiverAccountId(200L)
				.type(DEPOSIT)
				.status(UNPROCESSED)
				.date(Instant.now())
				.label("deposit of 100 euros")
				.build();

		transactionService.process(transaction);
		verify(transactionTypeProcessingServicePort).deposit(transaction);
		verifyNoMoreInteractions(transactionTypeProcessingServicePort);
	}
}
