package com.cdx.bas.application.bank.transaction;

import com.cdx.bas.application.bank.account.BankAccountTestUtils;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountException;
import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionException;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatusServicePort;
import com.cdx.bas.domain.bank.transaction.type.TransactionTypeProcessingServicePort;
import com.cdx.bas.domain.money.Money;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@QuarkusTest
class TransactionTypeProcessingServiceImplTest {

    @InjectMock
    TransactionStatusServicePort transactionStatusService;

    @InjectMock
    BankAccountServicePort bankAccountService;

    @Inject
    TransactionTypeProcessingServicePort transactionProcessingService;

    @Test
    public void credit_shouldThrowNoSuchElementException_whenEmitterAccountIsNotFound() {
        long emitterAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Money.of(new BigDecimal("0"));
        Instant instantDate = Instant.now();
        Transaction transaction = TransactionTestUtils.createTransactionUtils(emitterAccountId, receiverAccountId, new BigDecimal("1000"), UNPROCESSED, instantDate, new HashMap<>());
        Map<String, String> metadata = Map.of("error", "Emitter bank account 99 is not found.");
        Transaction erroredTransaction = TransactionTestUtils.createTransactionUtils(emitterAccountId, receiverAccountId, new BigDecimal("1000"), ERROR, instantDate, metadata);

        when(bankAccountService.findBankAccount(emitterAccountId)).thenThrow(new NoSuchElementException("Emitter bank account 99 is not found."));
        when(transactionStatusService.setStatus(eq(transaction), eq(ERROR), eq(metadata))).thenReturn(erroredTransaction);

        Transaction returnedTransaction =  transactionProcessingService.credit(transaction);

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo(TransactionTestUtils.createTransactionUtils(emitterAccountId, receiverAccountId, new BigDecimal("1000"), ERROR, instantDate, metadata));
        verify(bankAccountService).findBankAccount(eq(emitterAccountId));
        verify(transactionStatusService).setStatus(eq(transaction), eq(ERROR), eq(metadata));
        verifyNoMoreInteractions(bankAccountService, transactionStatusService);
    }

    @Test
    public void credit_shouldThrowNoSuchElementException_whenReceiverAccountIsNotFound() {
        long emitterAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Money.of(new BigDecimal("0"));
        Instant instantDate = Instant.now();
        Transaction transaction = TransactionTestUtils.createTransactionUtils(emitterAccountId, receiverAccountId, new BigDecimal("1000"), UNPROCESSED, instantDate, new HashMap<>());
        Map<String, String> metadata = Map.of("error", "Receiver bank account 77 is not found.");
        Transaction erroredTransaction = TransactionTestUtils.createTransactionUtils(emitterAccountId, receiverAccountId, new BigDecimal("1000"), ERROR, instantDate, metadata);
        BankAccount emitterBankAccount = BankAccountTestUtils.createBankAccountUtils(emitterAccountId, "0", transaction);

        when(bankAccountService.findBankAccount(emitterAccountId)).thenReturn(emitterBankAccount);
        when(bankAccountService.findBankAccount(receiverAccountId)).thenThrow(new NoSuchElementException("Receiver bank account 77 is not found."));
        when(transactionStatusService.setStatus(eq(transaction), eq(ERROR), eq(metadata))).thenReturn(erroredTransaction);

        Transaction returnedTransaction =  transactionProcessingService.credit(transaction);

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo (TransactionTestUtils.createTransactionUtils(emitterAccountId, receiverAccountId, new BigDecimal("1000"), ERROR, instantDate, metadata));
        verify(bankAccountService).findBankAccount(eq(emitterAccountId));
        verify(bankAccountService).findBankAccount(eq(receiverAccountId));
        verify(transactionStatusService).setStatus(eq(transaction), eq(ERROR), eq(metadata));
        verifyNoMoreInteractions(bankAccountService, transactionStatusService);
    }

    @Test
    public void credit_shouldReturnCompletedTransaction_whenAccountsAreFoundAndCreditTransactionIsValid() {
        long emitterBankAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Mockito.mock(Money.class);
        Instant instantDate = Instant.now();
        Transaction transaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("1000"), UNPROCESSED, instantDate, new HashMap<>());
        Transaction outstandingTransaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("1000"), OUTSTANDING, instantDate, new HashMap<>());
        BankAccount emitterBankAccount = BankAccountTestUtils.createBankAccountUtils(emitterBankAccountId, amountOfMoney, transaction);
        BankAccount receiverBankAccount = BankAccountTestUtils.createBankAccountUtils(receiverAccountId, amountOfMoney);

        Map<String, String> metadataAfter = Map.of("emitter_amount_before", "1000",
                "receiver_amount_before", "0",
                "emitter_amount_after", "0",
                "receiver_amount_after", "1000");
        Transaction completedTransaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("1000"), COMPLETED, instantDate, metadataAfter);
        BankAccount updatedEmitterBankAccount = BankAccountTestUtils.createBankAccountUtils(emitterBankAccountId, amountOfMoney, completedTransaction);

        when(bankAccountService.findBankAccount(emitterBankAccountId)).thenReturn(emitterBankAccount);
        when(bankAccountService.findBankAccount(receiverAccountId)).thenReturn(receiverBankAccount);
        when(transactionStatusService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        when(amountOfMoney.getAmount())
                .thenReturn(new BigDecimal("1000"))
                .thenReturn(new BigDecimal("0"))
                .thenReturn(new BigDecimal("0"))
                .thenReturn(new BigDecimal("1000"));
        when(transactionStatusService.setStatus(outstandingTransaction, COMPLETED, metadataAfter)).thenReturn(completedTransaction);
        when(bankAccountService.addTransaction(completedTransaction, emitterBankAccount)).thenReturn(updatedEmitterBankAccount);

        Transaction returnedTransaction =  transactionProcessingService.credit(transaction);

        receiverBankAccount.getBalance().plus(emitterBankAccount.getBalance());
        emitterBankAccount.getBalance().minus(emitterBankAccount.getBalance());

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo (TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("1000"), COMPLETED, instantDate, metadataAfter));
    }

    @Test
    public void credit_shouldAddMoneyToTheReceiverAccount_whenAccountsAreFound_fromCreditTransaction() {
        long emitterBankAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Mockito.mock(Money.class);
        Instant instantDate = Instant.now();
        Transaction oldTransaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("1000"), UNPROCESSED, instantDate, new HashMap<>());
        BankAccount emitterBankAccount = BankAccountTestUtils.createBankAccountUtils(emitterBankAccountId, amountOfMoney, oldTransaction);
        BankAccount receiverBankAccount = BankAccountTestUtils.createBankAccountUtils(receiverAccountId, amountOfMoney);

        Transaction transaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("1000"), UNPROCESSED, instantDate, new HashMap<>());
        Transaction outstandingTransaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("1000"), OUTSTANDING, instantDate, new HashMap<>());

        Map<String, String> metadataAfter = Map.of("emitter_amount_before", "1000",
                "receiver_amount_before", "0",
                "emitter_amount_after", "0",
                "receiver_amount_after", "1000");
        Transaction completedTransaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("1000"), COMPLETED, instantDate, metadataAfter);
        BankAccount updatedEmitterBankAccount = BankAccountTestUtils.createBankAccountUtils(emitterBankAccountId, amountOfMoney, completedTransaction);

        when(bankAccountService.findBankAccount(emitterBankAccountId)).thenReturn(emitterBankAccount);
        when(bankAccountService.findBankAccount(receiverAccountId)).thenReturn(receiverBankAccount);
        when(transactionStatusService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        when(amountOfMoney.getAmount())
                .thenReturn(new BigDecimal("1000"))
                .thenReturn(new BigDecimal("0"))
                .thenReturn(new BigDecimal("0"))
                .thenReturn(new BigDecimal("1000"));
        when(transactionStatusService.setStatus(outstandingTransaction, COMPLETED, metadataAfter)).thenReturn(completedTransaction);
        when(bankAccountService.addTransaction(completedTransaction, emitterBankAccount)).thenReturn(updatedEmitterBankAccount);

        transactionProcessingService.credit(transaction);

        verify(bankAccountService).findBankAccount(eq(emitterBankAccountId));
        verify(bankAccountService).findBankAccount(eq(receiverAccountId));
        verify(transactionStatusService).setAsOutstanding(transaction);
        verify(bankAccountService).transferAmountBetweenAccounts(outstandingTransaction, emitterBankAccount, receiverBankAccount);
        verify(transactionStatusService).setStatus(eq(outstandingTransaction), eq(COMPLETED), eq(metadataAfter));
        verify(bankAccountService).addTransaction(completedTransaction, emitterBankAccount);
        verify(bankAccountService).updateBankAccount(updatedEmitterBankAccount);
        verify(bankAccountService).updateBankAccount(receiverBankAccount);
        verifyNoMoreInteractions(bankAccountService, transactionStatusService);
    }

    @Test
    public void credit_shouldReturnRefusedTransaction_whenTransferThrowTransactionExceptionException() {
        long emitterBankAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Mockito.mock(Money.class);
        Instant instantDate = Instant.now();
        Transaction oldTransaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("-1000"), UNPROCESSED, instantDate, new HashMap<>());
        BankAccount emitterBankAccount = BankAccountTestUtils.createBankAccountUtils(emitterBankAccountId, amountOfMoney, oldTransaction);
        BankAccount receiverBankAccount = BankAccountTestUtils.createBankAccountUtils(receiverAccountId, amountOfMoney);

        Transaction transaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("-1000"), UNPROCESSED, instantDate, new HashMap<>());
        Transaction outstandingTransaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverAccountId, new BigDecimal("-1000"), OUTSTANDING, instantDate, new HashMap<>());
        Map<String, String> metadataAfter = Map.of("error", "Credit transaction 1 should have positive value, actual value: -1000");

        when(bankAccountService.findBankAccount(emitterBankAccountId)).thenReturn(emitterBankAccount);
        when(bankAccountService.findBankAccount(receiverAccountId)).thenReturn(receiverBankAccount);
        when(transactionStatusService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        when(amountOfMoney.getAmount())
                .thenReturn(new BigDecimal("1000"))
                .thenReturn(new BigDecimal("0"));
        doThrow(new TransactionException("Credit transaction 1 should have positive value, actual value: -1000"))
                .when(bankAccountService).transferAmountBetweenAccounts(outstandingTransaction, emitterBankAccount, receiverBankAccount);

        transactionProcessingService.credit(transaction);

        verify(bankAccountService).findBankAccount(eq(emitterBankAccountId));
        verify(bankAccountService).findBankAccount(eq(receiverAccountId));
        verify(transactionStatusService).setAsOutstanding(transaction);
        verify(bankAccountService).transferAmountBetweenAccounts(outstandingTransaction, emitterBankAccount, receiverBankAccount);
        verify(transactionStatusService).setStatus(transaction, REFUSED, metadataAfter);
        verifyNoMoreInteractions(bankAccountService, transactionStatusService);
    }

    @Test
    public void credit_shouldReturnErroredTransaction_whenAddTransactionThrowsBankAccountException() {
        long emitterBankAccountId = 99L;
        long receiverBankAccountId = 77L;
        Money amountOfMoney = Mockito.mock(Money.class);
        Instant instantDate = Instant.now();
        Transaction oldTransaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverBankAccountId, new BigDecimal("1000"), UNPROCESSED, instantDate, new HashMap<>());
        BankAccount emitterBankAccount = BankAccountTestUtils.createBankAccountUtils(emitterBankAccountId, amountOfMoney, oldTransaction);
        BankAccount receiverBankAccount = BankAccountTestUtils.createBankAccountUtils(receiverBankAccountId, amountOfMoney);

        Transaction transaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverBankAccountId, new BigDecimal("1000"), UNPROCESSED, instantDate, new HashMap<>());
        Transaction outstandingTransaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverBankAccountId, new BigDecimal("1000"), OUTSTANDING, instantDate, new HashMap<>());

        Map<String, String> metadataDuringProcess = Map.of("emitter_amount_before", "0",
                "receiver_amount_before", "0",
                "emitter_amount_after", "-1000",
                "receiver_amount_after", "1000");
        Transaction completedTransaction = TransactionTestUtils.createTransactionUtils(emitterBankAccountId, receiverBankAccountId, new BigDecimal("1000"), COMPLETED, instantDate, new HashMap<>());
        BankAccount updatedEmitterBankAccount = BankAccountTestUtils.createBankAccountUtils(emitterBankAccountId, amountOfMoney, completedTransaction);
        Map<String, String> metadataAfterError = Map.of("error", "Amount of credit should not be negative");

        when(bankAccountService.findBankAccount(emitterBankAccountId)).thenReturn(emitterBankAccount);
        when(bankAccountService.findBankAccount(receiverBankAccountId)).thenReturn(receiverBankAccount);
        when(transactionStatusService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        when(amountOfMoney.getAmount())
                .thenReturn(new BigDecimal("0"))
                .thenReturn(new BigDecimal("0"))
                .thenReturn(new BigDecimal("-1000"))
                .thenReturn(new BigDecimal("1000"));
        when(transactionStatusService.setStatus(outstandingTransaction, COMPLETED, metadataDuringProcess)).thenReturn(completedTransaction);
        when(bankAccountService.addTransaction(completedTransaction, emitterBankAccount)).thenReturn(updatedEmitterBankAccount);
        doThrow(new BankAccountException("Amount of credit should not be negative")).when(bankAccountService).updateBankAccount(updatedEmitterBankAccount);

        transactionProcessingService.credit(transaction);

        verify(bankAccountService).findBankAccount(eq(emitterBankAccountId));
        verify(bankAccountService).findBankAccount(eq(receiverBankAccountId));
        verify(transactionStatusService).setAsOutstanding(transaction);
        verify(bankAccountService).transferAmountBetweenAccounts(outstandingTransaction, emitterBankAccount, receiverBankAccount);
        verify(transactionStatusService).setStatus(outstandingTransaction, COMPLETED, metadataDuringProcess);
        verify(bankAccountService).addTransaction(completedTransaction, emitterBankAccount);
        verify(bankAccountService).updateBankAccount(updatedEmitterBankAccount);
        verify(transactionStatusService).setStatus(transaction, REFUSED, metadataAfterError);
        verifyNoMoreInteractions(bankAccountService, transactionStatusService);
    }
}