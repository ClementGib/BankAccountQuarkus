package com.cdx.bas.domain.bank.transaction;

import static com.cdx.bas.domain.bank.transaction.TransactionStatus.WAITING;
import static com.cdx.bas.domain.bank.transaction.TransactionType.CREDIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.Instant;

import javax.inject.Inject;

import com.cdx.bas.domain.bank.account.BankAccountService;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class TransactionServiceImplTest {

    @Inject
    TransactionService transactionService;
    
    @InjectMock
    BankAccountService bankAccountService;
    
    @Test
    public void processTransaction_should_processBankAccountDeposit_when_creditTransactionWithPositiveAmount() {
        Transaction transaction = new Transaction(10L, 100L, CREDIT, WAITING, Instant.now(), "deposit of 100 euros");
        transactionService.processTransaction(transaction);
        
        verify(bankAccountService).deposit(transaction);
        verifyNoMoreInteractions(bankAccountService);
    }
    
    @Test
    public void processTransaction_should_throwIllegalStateException_when_transactionIsInvalid() {
        try {
            Instant date = Instant.now();
            Transaction transaction = new Transaction(99L, -100L, TransactionType.CREDIT, WAITING, date, "Deposit of 100 euros");
            transactionService.processTransaction(transaction);

            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("Credit transaction amount must be greater than 0.\n");
        }
    }
}
