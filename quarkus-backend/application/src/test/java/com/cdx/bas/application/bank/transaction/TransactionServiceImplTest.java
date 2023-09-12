package com.cdx.bas.application.bank.transaction;

import static com.cdx.bas.domain.transaction.TransactionStatus.UNPROCESSED;
import static com.cdx.bas.domain.transaction.TransactionType.CREDIT;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.math.BigDecimal;
import java.time.Instant;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;

import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionServicePort;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TransactionServiceImplTest {

	@Inject
	TransactionServicePort transactionService;

	@InjectMock
	BankAccountServicePort bankAccountService;

	@Test
	public void processTransaction_should_processBankAccountDeposit_when_creditTransactionWithPositiveAmount() {
		Transaction transaction = new Transaction();
		transaction.setId(1L);
		transaction.setAmount(new BigDecimal(100));
		transaction.setAccountId(100L);
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
		transaction.setAccountId(991L);
		transaction.setType(CREDIT);
		transaction.setStatus(UNPROCESSED);
		transaction.setDate(Instant.now());
		transaction.setLabel("deposit of -100 euros");
		transactionService.process(transaction);

		verify(bankAccountService).deposit(transaction);
		verifyNoMoreInteractions(bankAccountService);
	}
}
