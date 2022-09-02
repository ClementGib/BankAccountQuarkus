package com.cdx.bas.domain.bank.account;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import com.cdx.bas.domain.bank.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;
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
        
        when(bankAccountManager.findById(accountId)).thenThrow(new NoSuchElementException("BankAccount 99L is not found."));
        
        try {
            bankAccountService.deposit(accountId, amountOfMoney);
            fail();
        } catch (NoSuchElementException exception) {
            verify(bankAccountManager).findById(eq(accountId));
        }
    }
    
    @Test
    public void deposit_should_addToMoneyToTheSpecificAccount_when_accountIsFound() {
        long accountId = 99L;
        Money amountOfMoney = Money.of(1000L);
        BankAccount bankAccount = createBankAccount(accountId);
        BankAccount bankAccountAfterDeposit = bankAccount;
        bankAccountAfterDeposit.getBalance().plus(amountOfMoney);
        
        when(bankAccountManager.findById(accountId)).thenReturn(bankAccount);
        
        bankAccountService.deposit(accountId, amountOfMoney);
        
        verify(bankAccountManager).findById(eq(accountId));
        verify(bankAccountManager).update(bankAccountAfterDeposit);
        
    }
    
    private BankAccount createBankAccount(long accountId) {
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
        history.add(new Transaction(accountId, 500L, TransactionType.CREDIT, firstTransactionDate, "First withdrawal to my bank account"));
        bankAccount.setHistory(history);
        return bankAccount;
    }
}
