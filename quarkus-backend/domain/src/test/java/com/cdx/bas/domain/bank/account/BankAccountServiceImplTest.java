package com.cdx.bas.domain.bank.account;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

import javax.inject.Inject;

import com.cdx.bas.domain.bank.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionService;
import com.cdx.bas.domain.bank.transaction.TransactionType;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class BankAccountServiceImplTest {
    
    @Inject
    TransactionService transactionService;
    
    @InjectMock
    BankAccountManager bankAccountManager;

    @Test
    public void deposit_should_addToMoneyToTheSpecificAccount_when_accountIsFound() {
        BankAccount bankAccount = createBankAccount();
        
        
//        verify(bankAccountManager).update(bankAccount);
        
    }
    
    @Test
    public void deposit_should_ThrowBankAccountBusinessException_when_MoneyIsNegative() {
    }
 
    
//    private BankAccount createValidBankAccount() {
//        ArrayList<Long> ownersId = new ArrayList<>();
//        ownersId.add(99L);
//        Instant lastTransactionDate = Instant.now();
//        ArrayList<Transaction> transactions = new ArrayList<>();
//        transactions.add(new Transaction(100, TransactionType.CREDIT, lastTransactionDate, "More withdrawal to my bank account"));
//        Instant firstTransactionDate = Instant.now();
//        ArrayList<Transaction> history = new ArrayList<>();
//        history.add(new Transaction(500, TransactionType.CREDIT, firstTransactionDate, "First withdrawal to my bank account"));
//        BankAccount BankAccount = new BankAccount();
//        BankAccount.setId(10L);
//        BankAccount.setType(AccountType.CHECKING);
//        BankAccount.setBalance(new Money(new BigDecimal("100")));
//        BankAccount.setOwnersId(ownersId);
//        BankAccount.setTransactions(transactions);
//        BankAccount.setHistory(history);
//        
//        BankAccount.validate();
//        return BankAccount;
//    }
    
    private BankAccount createBankAccount() {
        BankAccount bankAccount = new BankAccount();
        long accountId = 10L;
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
