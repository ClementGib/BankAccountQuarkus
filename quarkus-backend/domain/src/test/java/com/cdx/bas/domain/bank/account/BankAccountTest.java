package com.cdx.bas.domain.bank.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BankAccountTest {

    @Test
    public void validate_should_returnValidBankAccountObject_when_fillAllFieldsWithValidValues() {
        BankAccount bankAccount = new BankAccount();
        long accountId = 10L;
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(new Money(new BigDecimal("100")));
        ArrayList<Long> ownersId = new ArrayList<>();
        ownersId.add(99L);
        bankAccount.setOwnersId(ownersId);
        Instant lastTransactionDate = Instant.now();
        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(accountId, 100L, TransactionType.CREDIT, TransactionStatus.WAITING, lastTransactionDate, "More withdrawal to my bank account"));
        bankAccount.setTransactions(transactions);
        Instant firstTransactionDate = Instant.now();
        ArrayList<Transaction> history = new ArrayList<>();
        history.add(new Transaction(accountId, 500L, TransactionType.CREDIT, TransactionStatus.WAITING, firstTransactionDate, "First withdrawal to my bank account"));
        bankAccount.setHistory(history);
        
        bankAccount.validate();

        assertThat(bankAccount.getId()).isEqualTo(10);
        assertThat(bankAccount.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(bankAccount.getBalance()).usingRecursiveComparison().isEqualTo(Money.of(100L));
        assertThat(bankAccount.getOwnersId().size()).isEqualTo(1);
        assertThat(bankAccount.getOwnersId().get(0)).isEqualTo(99L);
        assertThat(bankAccount.getTransactions().size()).isEqualTo(1L);
        assertThat(bankAccount.getTransactions().get(0)).usingRecursiveComparison()
        .isEqualTo(new Transaction(accountId, 100L, TransactionType.CREDIT, TransactionStatus.WAITING, lastTransactionDate, "More withdrawal to my bank account"));
        assertThat(bankAccount.getHistory().size()).isEqualTo(1L);
        assertThat(bankAccount.getHistory().get(0)).usingRecursiveComparison()
        .isEqualTo(new Transaction(accountId, 500L, TransactionType.CREDIT, TransactionStatus.WAITING, firstTransactionDate, "First withdrawal to my bank account"));
    }

    @Test
    public void validate_should_returnValidBankAccountObject_when_fillRequiredFieldsWithValidValues() {
        BankAccount BankAccount = new BankAccount();
        BankAccount.setId(10L);
        BankAccount.setType(AccountType.CHECKING);
        BankAccount.setBalance(new Money(new BigDecimal("100")));
        ArrayList<Long> ownersId = new ArrayList<>();
        ownersId.add(99L);
        BankAccount.setOwnersId(ownersId);
        
        BankAccount.validate();

        assertThat(BankAccount.getId()).isEqualTo(10);
        assertThat(BankAccount.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(BankAccount.getBalance()).usingRecursiveComparison().isEqualTo(Money.of(100L));
        assertThat(BankAccount.getOwnersId().size()).isEqualTo(1);
        assertThat(BankAccount.getOwnersId().get(0)).isEqualTo(99L);
        assertThat(BankAccount.getTransactions().size()).isEqualTo(0);
        assertThat(BankAccount.getHistory().size()).isEqualTo(0);

    }
    
    @Test
    public void validate_should_throwIllegalStateExceptionWithRequiredFieldsNotBeNullMessage_requiredFieldsAreNull() {
        try {
            BankAccount bankAccount = new BankAccount(null, null, null, null, null, null);
            bankAccount.validate();

            fail();
        } catch (IllegalStateException exception) {
            String[] errorMessages = exception.getMessage().split("\n");
            assertThat(errorMessages).hasSize(6);
            assertThat(errorMessages[0]).hasToString("id must not be null.");
            assertThat(errorMessages[1]).hasToString("type must not be null.");
            assertThat(errorMessages[2]).hasToString("balance must not be null.");
            assertThat(errorMessages[3]).hasToString("ownersId must not be null.");
            assertThat(errorMessages[4]).hasToString("transactions must not be null.");
            assertThat(errorMessages[5]).hasToString("history must not be null.");
        }
    }
    
    @Test
    public void validate_should_throwIllegalStateExceptionWithSpecificMessages_when_idIsLowerThanOne() {
        try {
            long accountId = 0L;
            BankAccount bankAccount = new BankAccount();
            bankAccount.setId(accountId);
            bankAccount.setType(AccountType.CHECKING);
            bankAccount.setBalance(new Money(new BigDecimal("100")));
            ArrayList<Long> ownersId = new ArrayList<>();
            ownersId.add(99L);
            bankAccount.setOwnersId(ownersId);
            Instant lastTransactionDate = Instant.now();
            ArrayList<Transaction> transactions = new ArrayList<>();
            transactions.add(new Transaction(accountId, 100L, TransactionType.CREDIT, TransactionStatus.WAITING, lastTransactionDate, "More withdrawal to my bank account"));
            bankAccount.setTransactions(transactions);
            Instant firstTransactionDate = Instant.now();
            ArrayList<Transaction> history = new ArrayList<>();
            history.add(new Transaction(accountId, 500L, TransactionType.CREDIT, TransactionStatus.WAITING, firstTransactionDate, "First withdrawal to my bank account"));
            bankAccount.setHistory(history);
            
            bankAccount.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("id must be positive and higher than 0.\n");
        }
    }
    
    @Test
    public void validate_should_throwIllegalStateExceptionWithSpecificMessages_when_ownerIdsIsEmpty() {
        try {
            BankAccount bankAccount = new BankAccount();
            long accountId = 10L;
            bankAccount.setId(accountId);
            bankAccount.setType(AccountType.CHECKING);
            bankAccount.setBalance(new Money(new BigDecimal("100")));
            bankAccount.setOwnersId(new ArrayList<>());
            Instant lastTransactionDate = Instant.now();
            ArrayList<Transaction> transactions = new ArrayList<>();
            transactions.add(new Transaction(accountId, 100L, TransactionType.CREDIT, TransactionStatus.WAITING, lastTransactionDate, "More withdrawal to my bank account"));
            bankAccount.setTransactions(transactions);
            Instant firstTransactionDate = Instant.now();
            ArrayList<Transaction> history = new ArrayList<>();
            history.add(new Transaction(accountId, 500L, TransactionType.CREDIT, TransactionStatus.WAITING, firstTransactionDate, "First withdrawal to my bank account"));
            bankAccount.setHistory(history);
            
            bankAccount.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("ownersId must contain at least 1 owner id.\n");
        }
    }
}
