package com.cdx.bas.application.bank.account;

import static com.cdx.bas.domain.transaction.TransactionStatus.COMPLETED;
import static com.cdx.bas.domain.transaction.TransactionStatus.ERROR;
import static com.cdx.bas.domain.transaction.TransactionStatus.REFUSED;
import static com.cdx.bas.domain.transaction.TransactionStatus.WAITING;
import static com.cdx.bas.domain.transaction.TransactionType.CREDIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;

import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountException;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;
import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.bank.account.BankAccountValidator;
import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BankAccountServiceImplTest {
    
    @Inject
    BankAccountServicePort bankAccountService;
    
    @InjectMock
    BankAccountPersistencePort bankAccountPersistence;
    
    @Inject
    BankAccountValidator bankAccountValidator;

    @Test
    public void deposit_should_throwNoSuchElementException_when_accountIsFound() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(1000L);
        Instant date = Instant.now();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("error", "bank account 99 is not found.");
        Transaction transaction = createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, WAITING, date, metadata);
        when(bankAccountPersistence.findById(accountId)).thenThrow(new NoSuchElementException("bank account 99 is not found."));
        
        Transaction returnedTransaction =  bankAccountService.deposit(transaction);
        
        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, ERROR, date, metadata));
        verify(bankAccountPersistence).findById(eq(accountId));
        verifyNoMoreInteractions(bankAccountPersistence);
    }
    
    @Test
    public void deposit_should_addToMoneyToTheSpecificAccount_when_accountIsFound() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(1000L);
        Instant date = Instant.now();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("amount_before", "100");
        metadata.put("amount_after", "1100");
        Transaction transaction = createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, WAITING, date, metadata);
        BankAccount bankAccount = createBankAccount(accountId);
        when(bankAccountPersistence.findById(anyLong())).thenReturn(Optional.of(bankAccount));
        BankAccount bankAccountAfterDeposit = createBankAccount(accountId);
        bankAccountAfterDeposit.getBalance().plus(amountOfMoney);
        
        Transaction returnedTransaction =  bankAccountService.deposit(transaction);
        
        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, COMPLETED, date, metadata));
        verify(bankAccountPersistence).findById(eq(accountId));
        verify(bankAccountPersistence).update(eq(bankAccount));
    }
    
    @Test
    public void deposit_should_returnErroredTransaction_when_bankAccountValidatorThrowsException() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(1000L);
        Instant date = Instant.now();
  
        Money moneyBefore = new Money(new BigDecimal("100000"));
        BankAccount bankAccount = createBankAccount(accountId);
        bankAccount.setBalance(moneyBefore); 
        BankAccount bankAccountAfterDeposit = createBankAccount(accountId);
        bankAccountAfterDeposit.setBalance(moneyBefore); 
        BankAccountException violationException = new BankAccountException("balance amount must be between -600 and 100000.\n");
        Map<String, String> metadataBefore = new HashMap<>();
        metadataBefore.put("amount_before", "100000");
        metadataBefore.put("error", violationException.getMessage());
        Transaction transaction = createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, WAITING, date, metadataBefore);
        
        when(bankAccountPersistence.findById(anyLong())).thenReturn(Optional.of(bankAccount));
        
        Transaction returnedTransaction =  bankAccountService.deposit(transaction);
        
        Map<String, String> metadataAfter = new HashMap<>();
        metadataAfter.put("amount_before", "100000");
        metadataAfter.put("error", violationException.getMessage());
        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, REFUSED, date, metadataAfter));
        verify(bankAccountPersistence).findById(eq(accountId));
    }
    
    private static BankAccount createBankAccount(long accountId) {
        BankAccount bankAccount = new CheckingBankAccount();
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(new Money(new BigDecimal("100")));
        List<Long> customersId = new ArrayList<>();
        customersId.add(99L);
        bankAccount.setCustomersId(customersId);
        Instant firstTransactionDate = Instant.now();
        HashSet<Transaction> transactionHistory = new HashSet<>();
        transactionHistory.add(createTransaction(accountId, 500L, TransactionType.CREDIT, TransactionStatus.COMPLETED, firstTransactionDate, new HashMap<>()));
        bankAccount.setTransactions(transactionHistory);
        return bankAccount;
    }
    
    private static Transaction createTransaction(long accountId, long amount, TransactionType type, 
            TransactionStatus status, Instant date, Map<String, String> metadata) {
		Transaction transaction = new Transaction();
		transaction.setId(1L);
		transaction.setAmount(amount);
		transaction.setAccountId(accountId);
		transaction.setType(type);
		transaction.setStatus(status);
		transaction.setDate(date);
		transaction.setLabel("transaction of " + amount);
		transaction.setMetadata(metadata);
		return transaction;
    }
}
