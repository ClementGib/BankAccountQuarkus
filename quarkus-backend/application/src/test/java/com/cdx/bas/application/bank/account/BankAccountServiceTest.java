package com.cdx.bas.application.bank.account;

import com.cdx.bas.domain.bank.account.*;
import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionServicePort;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static com.cdx.bas.domain.transaction.TransactionStatus.*;
import static com.cdx.bas.domain.transaction.TransactionType.CREDIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class BankAccountServiceTest {

    @InjectMock
    BankAccountPersistencePort bankAccountRepository;

    @InjectMock
    BankAccountValidator bankAccountValidator;

    @InjectMock
    TransactionServicePort transactionService;

    @Inject
    BankAccountServicePort bankAccountService;

    @Test
    public void deposit_should_throwNoSuchElementException_when_senderAccountIsNotFound() {
        long senderAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Money.of(new BigDecimal(0));
        Instant instantDate = Instant.now();
        Transaction transaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        Map<String, String> metadata = Map.of("error", "Sender bank account 99 is not found.");
        Transaction erroredTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadata);

        when(bankAccountRepository.findById(senderAccountId)).thenThrow(new NoSuchElementException("Sender bank account 99 is not found."));
        when(transactionService.setAsError(eq(transaction), eq(metadata))).thenReturn(erroredTransaction);

        Transaction returnedTransaction =  bankAccountService.deposit(transaction);

        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadata));
        verify(bankAccountRepository).findById(eq(senderAccountId));
        verify(transactionService).setAsError(eq(transaction), eq(metadata));
        verifyNoMoreInteractions(bankAccountRepository, transactionService);
        verifyNoInteractions(bankAccountValidator);
    }

    @Test
    public void deposit_should_throwNoSuchElementException_when_receiverAccountIsNotFound() {
        long senderAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Money.of(new BigDecimal(0));
        Instant instantDate = Instant.now();
        Transaction transaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        Map<String, String> metadata = Map.of("error", "Receiver bank account 77 is not found.");
        Transaction erroredTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadata);
        BankAccount senderBankAccount = createBankAccount(senderAccountId, "0", transaction);

        when(bankAccountRepository.findById(senderAccountId)).thenReturn(Optional.of(senderBankAccount));
        when(bankAccountRepository.findById(receiverAccountId)).thenThrow(new NoSuchElementException("Receiver bank account 77 is not found."));
        when(transactionService.setAsError(eq(transaction), eq(metadata))).thenReturn(erroredTransaction);

        Transaction returnedTransaction =  bankAccountService.deposit(transaction);

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo(createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadata));
        verify(bankAccountRepository).findById(eq(senderAccountId));
        verify(bankAccountRepository).findById(eq(receiverAccountId));
        verify(transactionService).setAsError(eq(transaction), eq(metadata));
        verifyNoMoreInteractions(bankAccountRepository, transactionService);
        verifyNoInteractions(bankAccountValidator);
    }

    @Test
    public void deposit_should_withdrawMoneyFromSenderAccount_and_addMoneyToTheReceiverAccount_and_returnCompletedTransaction_when_accountsAreFound_from_creditTransaction() {
        long senderAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Money.of(new BigDecimal(1000));
        Instant instantDate = Instant.now();
        Transaction oldTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), TransactionType.CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        BankAccount senderBankAccount = createBankAccount(senderAccountId, "1000", oldTransaction);
        BankAccount receiverBankAccount = createBankAccount(receiverAccountId, "0");

        Transaction transaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        Transaction outstandingTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, OUTSTANDING, instantDate, new HashMap<>());

        Map<String, String> metadataAfter = Map.of("sender_amount_before", "1000",
                "receiver_amount_before", "0",
                "sender_amount_after", "0",
                "receiver_amount_after", "1000");
        Transaction completedTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, COMPLETED, instantDate, metadataAfter);
        BankAccount updatedSenderBankAccount = createBankAccount(senderAccountId, "0", completedTransaction);
        BankAccount updatedReceiverBankAccount = createBankAccount(receiverAccountId, "1000");

        when(bankAccountRepository.findById(senderAccountId)).thenReturn(Optional.of(senderBankAccount));
        when(bankAccountRepository.findById(receiverAccountId)).thenReturn(Optional.of(receiverBankAccount));
        when(transactionService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        when(transactionService.setAsCompleted(outstandingTransaction, metadataAfter)).thenReturn(completedTransaction);
        when(transactionService.mergeTransactions(transaction, completedTransaction)).thenReturn(completedTransaction);
        when(bankAccountRepository.update(updatedSenderBankAccount)).thenReturn(updatedSenderBankAccount);

        Transaction returnedTransaction =  bankAccountService.deposit(transaction);

        receiverBankAccount.getBalance().plus(senderBankAccount.getBalance());
        senderBankAccount.getBalance().minus(senderBankAccount.getBalance());

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo(createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, COMPLETED, instantDate, metadataAfter));
        verify(bankAccountRepository).findById(eq(senderAccountId));
        verify(bankAccountRepository).findById(eq(receiverAccountId));
        verify(transactionService).setAsOutstanding(transaction);
        verify(bankAccountValidator).validateBankAccount(eq(senderBankAccount));
        verify(bankAccountValidator).validateBankAccount(eq(receiverBankAccount));
        verify(transactionService).setAsCompleted(eq(outstandingTransaction), eq(metadataAfter));
        verify(transactionService).mergeTransactions(transaction, completedTransaction);
        verify(bankAccountRepository).update(eq(updatedSenderBankAccount));
        verify(bankAccountRepository).update(eq(updatedReceiverBankAccount));
        verifyNoMoreInteractions(bankAccountRepository, transactionService, bankAccountValidator);
    }

    @Test
    public void deposit_should_returnErroredTransaction_when_amountIsNegative() {
        long senderAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Money.of(new BigDecimal(-1000));
        Instant instantDate = Instant.now();
        Transaction oldTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), TransactionType.CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        BankAccount senderBankAccount = createBankAccount(senderAccountId, "1000", oldTransaction);
        BankAccount receiverBankAccount = createBankAccount(receiverAccountId, "0");

        Transaction transaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        Transaction outstandingTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, OUTSTANDING, instantDate, new HashMap<>());

        Map<String, String> metadataAfter = Map.of("sender_amount_before", "1000",
                "receiver_amount_before", "0",
                "error", "Credit transaction 1 should have positive value, actual value: -1000");
        Transaction refusedTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadataAfter);

        when(bankAccountRepository.findById(senderAccountId)).thenReturn(Optional.of(senderBankAccount));
        when(bankAccountRepository.findById(receiverAccountId)).thenReturn(Optional.of(receiverBankAccount));
        when(transactionService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        when(transactionService.setAsRefused(eq(transaction), eq(metadataAfter))).thenReturn(refusedTransaction);

        Transaction returnedTransaction =  bankAccountService.deposit(transaction);

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo(createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadataAfter));
        verify(bankAccountRepository).findById(eq(senderAccountId));
        verify(bankAccountRepository).findById(eq(receiverAccountId));
        verify(transactionService).setAsOutstanding(transaction);
        verify(transactionService).setAsRefused(transaction, metadataAfter);
        verifyNoMoreInteractions(bankAccountRepository, transactionService, bankAccountValidator);
    }
    
    @Test
    public void deposit_should_returnErroredTransaction_when_senderBankAccountValidatorThrowsException() {
        long senderAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Money.of(new BigDecimal(1000));
        Instant instantDate = Instant.now();
        Transaction oldTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), TransactionType.CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        BankAccount senderBankAccount = createBankAccount(senderAccountId, "1000", oldTransaction);
        BankAccount receiverBankAccount = createBankAccount(receiverAccountId, "0");

        Transaction transaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        Transaction outstandingTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, OUTSTANDING, instantDate, new HashMap<>());

        Map<String, String> metadataAfter = Map.of("sender_amount_before", "1000",
                "receiver_amount_before", "0",
                "error", "Amount of credit should not be negative");
        Transaction refusedTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadataAfter);

        when(bankAccountRepository.findById(senderAccountId)).thenReturn(Optional.of(senderBankAccount));
        when(bankAccountRepository.findById(receiverAccountId)).thenReturn(Optional.of(receiverBankAccount));
        when(transactionService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        doThrow(new BankAccountException("Amount of credit should not be negative")).when(bankAccountValidator).validateBankAccount(senderBankAccount);
        when(transactionService.setAsRefused(eq(transaction), eq(metadataAfter))).thenReturn(refusedTransaction);

        Transaction returnedTransaction =  bankAccountService.deposit(transaction);

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo(createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadataAfter));
        verify(bankAccountRepository).findById(eq(senderAccountId));
        verify(bankAccountRepository).findById(eq(receiverAccountId));
        verify(transactionService).setAsOutstanding(transaction);
        verify(bankAccountValidator).validateBankAccount(senderBankAccount);
        verify(transactionService).setAsRefused(transaction, metadataAfter);
        verifyNoMoreInteractions(bankAccountRepository, transactionService, bankAccountValidator);
    }

    @Test
    public void deposit_should_returnErroredTransaction_when_receiverBankAccountValidatorThrowsException() {
        long senderAccountId = 99L;
        long receiverAccountId = 77L;
        Money amountOfMoney = Money.of(new BigDecimal(1000));
        Instant instantDate = Instant.now();
        Transaction oldTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), TransactionType.CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        BankAccount senderBankAccount = createBankAccount(senderAccountId, "1000", oldTransaction);
        BankAccount receiverBankAccount = createBankAccount(receiverAccountId, "0");

        Transaction transaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        Transaction outstandingTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, OUTSTANDING, instantDate, new HashMap<>());

        Map<String, String> metadataAfter = Map.of("sender_amount_before", "1000",
                "receiver_amount_before", "0",
                "error", "Amount of credit should not be negative");
        Transaction refusedTransaction = createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadataAfter);

        when(bankAccountRepository.findById(senderAccountId)).thenReturn(Optional.of(senderBankAccount));
        when(bankAccountRepository.findById(receiverAccountId)).thenReturn(Optional.of(receiverBankAccount));
        when(transactionService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        doThrow(new BankAccountException("Amount of credit should not be negative")).when(bankAccountValidator).validateBankAccount(receiverBankAccount);
        when(transactionService.setAsRefused(eq(transaction), eq(metadataAfter))).thenReturn(refusedTransaction);

        Transaction returnedTransaction =  bankAccountService.deposit(transaction);

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo(createTransaction(1L, senderAccountId, receiverAccountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadataAfter));
        verify(bankAccountRepository).findById(eq(senderAccountId));
        verify(bankAccountRepository).findById(eq(receiverAccountId));
        verify(transactionService).setAsOutstanding(transaction);
        verify(bankAccountValidator).validateBankAccount(senderBankAccount);
        verify(bankAccountValidator).validateBankAccount(receiverBankAccount);
        verify(transactionService).setAsRefused(transaction, metadataAfter);
        verifyNoMoreInteractions(bankAccountRepository, transactionService, bankAccountValidator);
    }

    private static BankAccount createBankAccount(long accountId, String amount, Transaction ...transactions) {
        BankAccount bankAccount = new CheckingBankAccount();
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(new Money(new BigDecimal(amount)));
        List<Long> customersId = new ArrayList<>();
        customersId.add(99L);
        bankAccount.setCustomersId(customersId);
        HashSet<Transaction> transactionHistory = new HashSet<>();
        Collections.addAll(transactionHistory, transactions);
        bankAccount.setIssuedTransactions(transactionHistory);
        return bankAccount;
    }
    
    private static Transaction createTransaction(long id, long senderAccountId, long receiverAccountId,
                                                 BigDecimal amount, TransactionType type, TransactionStatus status,
                                                 Instant date, Map<String, String> metadata) {
		Transaction transaction = new Transaction();
		transaction.setId(id);
		transaction.setAmount(amount);
        transaction.setCurrency("EUR");
		transaction.setSenderAccountId(senderAccountId);
        transaction.setReceiverAccountId(receiverAccountId);
		transaction.setType(type);
		transaction.setStatus(status);
		transaction.setDate(date);
		transaction.setLabel("transaction of " + amount);
		transaction.setMetadata(metadata);
		return transaction;
    }
}
