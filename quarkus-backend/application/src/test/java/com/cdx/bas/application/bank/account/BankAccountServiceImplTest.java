package com.cdx.bas.application.bank.account;

import static com.cdx.bas.domain.transaction.TransactionStatus.COMPLETED;
import static com.cdx.bas.domain.transaction.TransactionStatus.ERROR;
import static com.cdx.bas.domain.transaction.TransactionStatus.REFUSED;
import static com.cdx.bas.domain.transaction.TransactionStatus.WAITING;
import static com.cdx.bas.domain.transaction.TransactionType.CREDIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.inject.Inject;

import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountException;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;
import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class BankAccountServiceImplTest {
    
    @Inject
    BankAccountServicePort bankAccountService;
    
    @InjectMock
    BankAccountPersistencePort bankAccountPersistence;

    @Test
    public void deposit_should_throwNoSuchElementException_when_accountIsFound() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(1000L);
        Instant date = Instant.now();
        Transaction transaction = createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, WAITING, date);
        when(bankAccountPersistence.findById(accountId)).thenThrow(new NoSuchElementException("bank account 99L is not found."));
        
        Transaction returnedTransaction =  bankAccountService.deposit(transaction);
        
        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, ERROR, date));
        verify(bankAccountPersistence).findById(eq(accountId));
        verifyNoMoreInteractions(bankAccountPersistence);
    }
    
    @Test
    public void deposit_should_addToMoneyToTheSpecificAccount_when_accountIsFound() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(1000L);
        Instant date = Instant.now();
        Transaction transaction = createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, WAITING, date);
        BankAccount bankAccount = createBankAccount(accountId);
        when(bankAccountPersistence.findById(anyLong())).thenReturn(Optional.of(bankAccount));
        BankAccount bankAccountAfterDeposit = bankAccount;
        bankAccountAfterDeposit.getBalance().plus(amountOfMoney);
        
        Transaction returnedTransaction =  bankAccountService.deposit(transaction);
        
        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, COMPLETED, date));
        verify(bankAccountPersistence).findById(eq(accountId));
        verify(bankAccountPersistence).update(eq(bankAccountAfterDeposit));
    }
    
    @Test
    public void deposit_should_returnErroredTransaction_when_bankAccountPersistenceUpdateThrowsException() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(1000L);
        Instant date = Instant.now();
        Transaction transaction = createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, WAITING, date);
        BankAccount bankAccount = createBankAccount(accountId);
        when(bankAccountPersistence.findById(anyLong())).thenReturn(Optional.of(bankAccount));
        BankAccount bankAccountAfterDeposit = bankAccount;
        bankAccountAfterDeposit.getBalance().plus(amountOfMoney);
        when(bankAccountPersistence.update(any())).thenThrow(new BankAccountException("Bank account amount is already at the maximum."));
        
        Transaction returnedTransaction =  bankAccountService.deposit(transaction);
        
        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(createTransaction(accountId, amountOfMoney.getAmount().longValue(), CREDIT, REFUSED, date));
        verify(bankAccountPersistence).findById(eq(accountId));
        verify(bankAccountPersistence).update(eq(bankAccountAfterDeposit));
    }
    
    private static BankAccount createBankAccount(long accountId) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(new Money(new BigDecimal("100")));
        HashSet<Long> customersId = new HashSet<>();
        customersId.add(99L);
        bankAccount.setCustomersId(customersId);
        bankAccount.setTransactions(new HashSet<>());
        Instant firstTransactionDate = Instant.now();
        HashSet<Transaction> history = new HashSet<>();
        history.add(createTransaction(accountId, 500L, TransactionType.CREDIT, TransactionStatus.COMPLETED, firstTransactionDate));
        bankAccount.setHistory(history);
        return bankAccount;
    }
    
    private static Transaction createTransaction(long accountId, long amount, TransactionType type, TransactionStatus status, Instant date) {
		Transaction transaction = new Transaction();
		transaction.setId(1L);
		transaction.setAmount(amount);
		transaction.setAccountId(accountId);
		transaction.setType(type);
		transaction.setStatus(status);
		transaction.setDate(date);
		transaction.setLabel("transaction of " + amount);
		return transaction;
    }
}
