package com.cdx.bas.application.bank.transaction;

import static com.cdx.bas.domain.transaction.TransactionStatus.UNPROCESSED;
import static com.cdx.bas.domain.transaction.TransactionType.CREDIT;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.math.BigDecimal;
import java.time.Instant;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import jakarta.inject.Inject;

import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionServicePort;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class TransactionServiceTest {

	@Inject
	TransactionServicePort transactionService;

	@InjectMock
	BankAccountServicePort bankAccountService;

	@Test
	public void processTransaction_should_processBankAccountDeposit_when_creditTransactionWithPositiveAmount() {
		Transaction transaction = new Transaction();
		transaction.setId(1L);
		transaction.setAmount(new BigDecimal(100));
		transaction.setSenderAccountId(100L);
		transaction.setReceiverAccountId(200L);
		transaction.setType(CREDIT);
		transaction.setStatus(UNPROCESSED);
		transaction.setDate(Instant.now());
		transaction.setLabel("deposit of 100 euros");
		transactionService.process(transaction);

		verify(bankAccountService).deposit(transaction);
		verifyNoMoreInteractions(bankAccountService);
	}

	@Test
	public void processTransaction_should_processBankAccountDeposit_when_creditTransactionWithNegativeAmount() {
		Transaction transaction = new Transaction();
		transaction.setId(1L);
		transaction.setAmount(new BigDecimal(-100));
		transaction.setSenderAccountId(99L);
		transaction.setType(CREDIT);
		transaction.setStatus(UNPROCESSED);
		transaction.setDate(Instant.now());
		transaction.setLabel("deposit of -100 euros");
		transactionService.process(transaction);

		verify(bankAccountService).deposit(transaction);
		verifyNoMoreInteractions(bankAccountService);
	}
}