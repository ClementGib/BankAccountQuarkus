package com.cdx.bas.domain.bank.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

import com.cdx.bas.domain.bank.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BankAccountTest {

    @Test
    public void validate_should_returnValidBankAccountObject_when_fillAllFieldsWithValidValues() {
        ArrayList<Long> ownersId = new ArrayList<>();
        ownersId.add(99L);
        Instant lastTransactionDate = Instant.now();
        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(100, TransactionType.CREDIT, lastTransactionDate, "More withdrawal to my bank account"));
        Instant firstTransactionDate = Instant.now();
        ArrayList<Transaction> history = new ArrayList<>();
        history.add(new Transaction(500, TransactionType.CREDIT, firstTransactionDate, "First withdrawal to my bank account"));
        BankAccount BankAccount = new BankAccount();
        BankAccount.setId(10L);
        BankAccount.setType(AccountType.CHECKING);
        BankAccount.setBalance(new Money(new BigDecimal("100")));
        BankAccount.setOwnersId(ownersId);
        BankAccount.setTransactions(transactions);
        BankAccount.setHistory(history);
        
        BankAccount.validate();

        assertThat(BankAccount.getId()).isEqualTo(10);
        assertThat(BankAccount.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(BankAccount.getBalance()).usingRecursiveComparison().isEqualTo(Money.of(100L));
        assertThat(BankAccount.getOwnersId().size()).isEqualTo(1);
        assertThat(BankAccount.getOwnersId().get(0)).isEqualTo(99L);
        assertThat(BankAccount.getTransactions().size()).isEqualTo(1L);
        assertThat(BankAccount.getTransactions().get(0)).usingRecursiveComparison()
        .isEqualTo(new Transaction(100, TransactionType.CREDIT, lastTransactionDate, "More withdrawal to my bank account"));
        assertThat(BankAccount.getHistory().size()).isEqualTo(1L);
        assertThat(BankAccount.getHistory().get(0)).usingRecursiveComparison()
        .isEqualTo(new Transaction(500, TransactionType.CREDIT, firstTransactionDate, "First withdrawal to my bank account"));
    }

    @Test
    public void validate_should_returnValidBankAccountObject_when_fillRequiredFieldsWithValidValues() {
        ArrayList<Long> ownersId = new ArrayList<>();
        ownersId.add(99L);
        BankAccount BankAccount = new BankAccount();
        BankAccount.setId(10L);
        BankAccount.setType(AccountType.CHECKING);
        BankAccount.setBalance(new Money(new BigDecimal("100")));
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
            ArrayList<Long> ownersId = new ArrayList<>();
            ownersId.add(99L);
            Instant lastTransactionDate = Instant.now();
            ArrayList<Transaction> transactions = new ArrayList<>();
            transactions.add(new Transaction(100, TransactionType.CREDIT, lastTransactionDate, "More withdrawal to my bank account"));
            Instant firstTransactionDate = Instant.now();
            ArrayList<Transaction> history = new ArrayList<>();
            history.add(new Transaction(500, TransactionType.CREDIT, firstTransactionDate, "First withdrawal to my bank account"));
            BankAccount BankAccount = new BankAccount();
            BankAccount.setId(0L);
            BankAccount.setType(AccountType.CHECKING);
            BankAccount.setBalance(new Money(new BigDecimal("100")));
            BankAccount.setOwnersId(ownersId);
            BankAccount.setTransactions(transactions);
            BankAccount.setHistory(history);
            
            BankAccount.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("id must be positive and higher than 0.\n");
        }
    }
    
    @Test
    public void validate_should_throwIllegalStateExceptionWithSpecificMessages_when_ownerIdsIsEmpty() {
        try {
            Instant lastTransactionDate = Instant.now();
            ArrayList<Transaction> transactions = new ArrayList<>();
            transactions.add(new Transaction(100, TransactionType.CREDIT, lastTransactionDate, "More withdrawal to my bank account"));
            Instant firstTransactionDate = Instant.now();
            ArrayList<Transaction> history = new ArrayList<>();
            history.add(new Transaction(500, TransactionType.CREDIT, firstTransactionDate, "First withdrawal to my bank account"));
            
            BankAccount BankAccount = new BankAccount();
            BankAccount.setId(1L);
            BankAccount.setType(AccountType.CHECKING);
            BankAccount.setBalance(new Money(new BigDecimal("100")));
            BankAccount.setOwnersId(new ArrayList<>());
            BankAccount.setTransactions(transactions);
            BankAccount.setHistory(history);
            
            BankAccount.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("ownersId must contain at least 1 owner id.\n");
        }
    }
}
