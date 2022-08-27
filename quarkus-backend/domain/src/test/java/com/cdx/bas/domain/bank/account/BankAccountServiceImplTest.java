package com.cdx.bas.domain.bank.account;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

import com.cdx.bas.domain.bank.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BankAccountServiceImplTest {
    
    
    @Mock
    BankAccountManager bankAccountManager;

    @Test
    public void deposit_should_addToMoneyToTheSpecificAccount_when_MoneyIsValidAndPositive() {
        BankAccount bankAccount = new BankAccount();
        
        
        verify(bankAccountManager).update(bankAccount);
        
    }
    
    @Test
    public void deposit_should_ThrowBankAccountBusinessException_when_MoneyIsNegative() {
    }
 
    
    private BankAccount createValidBankAccount() {
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
        return BankAccount;
    }
}
