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
    public void deposit_should_throwNoSuchElementException_when_accountIsNotFound() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(new BigDecimal(0));
        Instant instantDate = Instant.now();
        Transaction transaction = createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        Map<String, String> metadata = Map.of("error", "bank account 99 is not found.");
        Transaction erroredTransaction = createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadata);

        when(bankAccountRepository.findById(accountId)).thenThrow(new NoSuchElementException("bank account 99 is not found."));
        when(transactionService.setAsError(eq(transaction), eq(metadata))).thenReturn(erroredTransaction);
        
        Transaction returnedTransaction =  bankAccountService.deposit(transaction);
        
        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadata));
        verify(bankAccountRepository).findById(eq(accountId));
        verify(transactionService).setAsError(eq(transaction), eq(metadata));
        verifyNoMoreInteractions(bankAccountRepository, transactionService);
        verifyNoInteractions(bankAccountValidator);
    }

    @Test
    public void deposit_should_addToMoneyToTheSpecificAccount_and_returnCompletedTransaction_and_updateTransaction_when_accountIsFound_with_currentTransaction() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(new BigDecimal(1000));
        Instant instantDate = Instant.now();
        Transaction oldTransaction = createTransaction(99L, accountId, amountOfMoney.getAmount(), TransactionType.CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        BankAccount bankAccount = createBankAccount(accountId, "0", oldTransaction);

        Transaction transaction = createTransaction(99L, accountId, amountOfMoney.getAmount(), CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        Transaction outstandingTransaction = createTransaction(99L, accountId, amountOfMoney.getAmount(), CREDIT, OUTSTANDING, instantDate, new HashMap<>());

        Map<String, String> metadataAfter = Map.of("amount_before", "0", "amount_after", "1000");
        Transaction completedTransaction = createTransaction(99L, accountId, amountOfMoney.getAmount(), CREDIT, COMPLETED, instantDate, metadataAfter);
        BankAccount updatedBankAccount = createBankAccount(accountId, "1000", completedTransaction);

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(bankAccount));
        when(transactionService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        when(transactionService.setAsCompleted(outstandingTransaction, metadataAfter)).thenReturn(completedTransaction);
        when(transactionService.mergeTransactions(transaction, completedTransaction)).thenReturn(completedTransaction);
        when(bankAccountRepository.update(updatedBankAccount)).thenReturn(updatedBankAccount);

        Transaction returnedTransaction =  bankAccountService.deposit(transaction);

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo(createTransaction(99L, accountId, amountOfMoney.getAmount(), CREDIT, COMPLETED, instantDate, metadataAfter));
        verify(bankAccountRepository).findById(eq(accountId));
        verify(transactionService).setAsOutstanding(transaction);
        verify(bankAccountValidator).validateBankAccount(bankAccount);
        verify(transactionService).setAsCompleted(eq(outstandingTransaction), eq(metadataAfter));
        verify(transactionService).mergeTransactions(transaction, completedTransaction);
        verify(bankAccountRepository).update(eq(updatedBankAccount));
        verifyNoMoreInteractions(bankAccountRepository, transactionService, bankAccountValidator);
    }
    
    @Test
    public void deposit_should_addToMoneyToTheSpecificAccount_and_returnCompletedTransaction_with_newTransaction_when_accountIsFound_without_currentTransaction() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(new BigDecimal(1000));
        Instant instantDate = Instant.now();
        Map<String, String> metadataBefore = Map.of("amount_before", "0", "amount_after", "100");

        Transaction oldTransaction = createTransaction(99L, accountId, new BigDecimal(100), TransactionType.CREDIT, TransactionStatus.COMPLETED, instantDate, metadataBefore);
        BankAccount bankAccount = createBankAccount(accountId, "100", oldTransaction);

        Transaction transaction = createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, UNPROCESSED, instantDate, new HashMap<>());
        Transaction outstandingTransaction = createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, OUTSTANDING, instantDate, new HashMap<>());

        Map<String, String> metadataAfter = new HashMap<>();
        metadataAfter.put("amount_before", "100");
        metadataAfter.put("amount_after", "1100");
        Transaction completedTransaction = createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, COMPLETED, instantDate, metadataAfter);
        BankAccount updatedBankAccount = createBankAccount(accountId, "1100", oldTransaction, completedTransaction);

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(bankAccount));
        when(transactionService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        when(transactionService.setAsCompleted(outstandingTransaction, metadataAfter)).thenReturn(completedTransaction);
        when(bankAccountRepository.update(updatedBankAccount)).thenReturn(updatedBankAccount);

        Transaction returnedTransaction =  bankAccountService.deposit(transaction);

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo(createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, COMPLETED, instantDate, metadataAfter));
        verify(bankAccountRepository).findById(eq(accountId));
        verify(transactionService).setAsOutstanding(transaction);
        verify(bankAccountValidator).validateBankAccount(bankAccount);
        verify(bankAccountRepository).update(eq(updatedBankAccount));
        verify(transactionService).setAsCompleted(eq(outstandingTransaction), eq(metadataAfter));
        verify(bankAccountRepository).update(eq(updatedBankAccount));
        verifyNoMoreInteractions(bankAccountRepository, transactionService);
    }
    
    @Test
    public void deposit_should_returnErroredTransaction_when_bankAccountValidatorThrowsException() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(new BigDecimal(1000));
        Instant instantDate = Instant.now();
        Map<String, String> metadataBefore = new HashMap<>();
        metadataBefore.put("amount_before", "0");
        metadataBefore.put("amount_after", "100");
        Transaction transaction = createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, UNPROCESSED, instantDate, metadataBefore);
        BankAccount bankAccount = createBankAccount(accountId, "100", (Transaction) null);
        Transaction outstandingTransaction = createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, OUTSTANDING, instantDate, metadataBefore);

        Map<String, String> metadataAfter = new HashMap<>();
        metadataAfter.put("amount_before", "100");
        metadataAfter.put("error", "Amount of credit should not be negative");
        Transaction refusedTransaction = createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadataAfter);

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(bankAccount));
        when(transactionService.setAsOutstanding(transaction)).thenReturn(outstandingTransaction);
        doThrow(new BankAccountException("Amount of credit should not be negative")).when(bankAccountValidator).validateBankAccount(bankAccount);
        when(transactionService.setAsRefused(eq(transaction), eq(metadataAfter))).thenReturn(refusedTransaction);

        Transaction returnedTransaction =  bankAccountService.deposit(transaction);

        assertThat(returnedTransaction).usingRecursiveComparison()
                .isEqualTo(createTransaction(1L, accountId, amountOfMoney.getAmount(), CREDIT, ERROR, instantDate, metadataAfter));verify(bankAccountRepository).findById(eq(accountId));
        verify(bankAccountRepository).findById(eq(accountId));
        verify(transactionService).setAsOutstanding(transaction);
        verify(bankAccountValidator).validateBankAccount(bankAccount);
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
        bankAccount.setTransactions(transactionHistory);
        return bankAccount;
    }
    
    private static Transaction createTransaction(long id, long accountId, BigDecimal amount, TransactionType type,
            TransactionStatus status, Instant date, Map<String, String> metadata) {
		Transaction transaction = new Transaction();
		transaction.setId(id);
		transaction.setAmount(amount);
        transaction.setCurrency("EUR");
		transaction.setSenderAccountId(accountId);
        transaction.setReceiverAccountId(77L);
		transaction.setType(type);
		transaction.setStatus(status);
		transaction.setDate(date);
		transaction.setLabel("transaction of " + amount);
		transaction.setMetadata(metadata);
		return transaction;
    }
}
