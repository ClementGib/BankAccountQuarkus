package com.cdx.bas.domain.bank.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.domain.bank.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.TransactionType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class BankAccountServiceImplTest {
    
    @Inject
    BankAccountService bankAccountService;
    
    @InjectMock
    BankAccountManager bankAccountManager;

    @Test
    public void deposit_should_throwNoSuchElementException_when_accountIsFound() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(1000L);
        Instant date = Instant.now();
        Transaction transaction = new Transaction(accountId, amountOfMoney.getAmount().longValue(), TransactionType.CREDIT, TransactionStatus.WAITING, date, "Deposit of 1000 euros");
        when(bankAccountManager.findById(accountId)).thenThrow(new NoSuchElementException("bank account 99L is not found."));
        
        Transaction returnedTransaction =  bankAccountService.deposit(transaction);
        
        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(new Transaction(accountId, amountOfMoney.getAmount().longValue(), TransactionType.CREDIT, TransactionStatus.ERROR, date, "Deposit of 1000 euros"));
        verify(bankAccountManager).findById(eq(accountId));
        verifyNoMoreInteractions(bankAccountManager);
    }
    
    @Test
    public void deposit_should_addToMoneyToTheSpecificAccount_when_accountIsFound() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(1000L);
        Instant date = Instant.now();
        Transaction transaction = new Transaction(accountId, amountOfMoney.getAmount().longValue(), TransactionType.CREDIT, TransactionStatus.WAITING, date, "Deposit of 1000 euros");
        BankAccount bankAccount = createBankAccount(accountId);
        when(bankAccountManager.findById(anyLong())).thenReturn(Optional.of(bankAccount));
        BankAccount bankAccountAfterDeposit = bankAccount;
        bankAccountAfterDeposit.getBalance().plus(amountOfMoney);
        
        Transaction returnedTransaction =  bankAccountService.deposit(transaction);
        
        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(new Transaction(accountId, amountOfMoney.getAmount().longValue(), TransactionType.CREDIT, TransactionStatus.COMPLETED, date, "Deposit of 1000 euros"));
        verify(bankAccountManager).findById(eq(accountId));
        verify(bankAccountManager).update(eq(bankAccountAfterDeposit));
    }
    
    @Test
    public void deposit_should_returnErroredTransaction_when_BankAccountManagerUpdateThrowsException() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(1000L);
        Instant date = Instant.now();
        Transaction transaction = new Transaction(accountId, amountOfMoney.getAmount().longValue(), TransactionType.CREDIT, TransactionStatus.WAITING, date, "Deposit of 1000 euros");
        BankAccount bankAccount = createBankAccount(accountId);
        when(bankAccountManager.findById(anyLong())).thenReturn(Optional.of(bankAccount));
        BankAccount bankAccountAfterDeposit = bankAccount;
        bankAccountAfterDeposit.getBalance().plus(amountOfMoney);
        when(bankAccountManager.update(any())).thenThrow(new BankAccountException("Bank account amount is already at the maximum."));
        
        Transaction returnedTransaction =  bankAccountService.deposit(transaction);
        
        assertThat(returnedTransaction).usingRecursiveComparison()
        .isEqualTo(new Transaction(accountId, amountOfMoney.getAmount().longValue(), TransactionType.CREDIT, TransactionStatus.REFUSED, date, "Deposit of 1000 euros"));
        verify(bankAccountManager).findById(eq(accountId));
        verify(bankAccountManager).update(eq(bankAccountAfterDeposit));
    }
    
    @RequestScoped
    public static class BankAccountManagerImplMock implements BankAccountManager {

        public Optional<BankAccount> findById(long id) {
            return Optional.of(createBankAccount(id));
        }

        @Override
        public BankAccount create(BankAccount bankAccount) {
            return bankAccount;
        }

        @Override
        public BankAccount update(BankAccount bankAccount) {
            return bankAccount;
        }
    }
    
    private static BankAccount createBankAccount(long accountId) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(new Money(new BigDecimal("100")));
        ArrayList<Long> ownersId = new ArrayList<>();
        ownersId.add(99L);
        bankAccount.setOwnersId(ownersId);
        bankAccount.setTransactions(new ArrayList<>());
        Instant firstTransactionDate = Instant.now();
        ArrayList<Transaction> history = new ArrayList<>();
        history.add(new Transaction(accountId, 500L, TransactionType.CREDIT, TransactionStatus.COMPLETED, firstTransactionDate, "First withdrawal to my bank account"));
        bankAccount.setHistory(history);
        return bankAccount;
    }
}
