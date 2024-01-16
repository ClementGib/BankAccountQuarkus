package com.cdx.bas.application.bank.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import jakarta.inject.Inject;

import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class BankAccountRepositoryTest {

    @Inject
    private BankAccountRepository bankAccountRepository;
    
    @InjectMock
    private DtoEntityMapper<BankAccount, BankAccountEntity> bankAccountMapper;
    
    @Test
    public void findById_should_returnBankAccount_when_accountIsFound() {
        long accountId = 1L;
        Instant date = Instant.now();
        BankAccount bankAccount = createBankAccount(accountId, date);
        
        when(bankAccountMapper.toDto(any())).thenReturn(bankAccount);
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(accountId);
        
        assertThat(optionalBankAccount.get()).usingRecursiveComparison().isEqualTo(bankAccount);
        verify(bankAccountMapper).toDto(any(BankAccountEntity.class));
        verifyNoMoreInteractions(bankAccountMapper);
    }
    
    @Test
    public void findById_should_returnEmptyOptionak_when_accountIsNotFound() {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(99999L);
        
        assertThat(optionalBankAccount).isEmpty();
        verifyNoInteractions(bankAccountMapper);
    }
    
    private BankAccount createBankAccount(long accountId, Instant instantDate) {
        BankAccount bankAccount = new CheckingBankAccount();
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(new Money(new BigDecimal("300")));
        List<Long> customersId = new ArrayList<>();
        customersId.add(1L);
        bankAccount.setCustomersId(customersId);
        HashSet<Transaction> transactionHistory = new HashSet<>();
        transactionHistory.add(createTransaction(2L, accountId, instantDate));
        bankAccount.setIssuedTransactions(transactionHistory);
        return bankAccount;
    }
    
    private Transaction createTransaction(long id, long accountId, Instant instantDate) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setSenderAccountId(accountId);
        transaction.setReceiverAccountId(77L);
        transaction.setAmount(new BigDecimal(100));
        transaction.setType(TransactionType.CREDIT);
        transaction.setStatus(TransactionStatus.ERROR);
        transaction.setDate(instantDate);
        transaction.setLabel("transaction test");
        Map<String, String> metadata = Map.of("amount_before", "0", "amount_after", "350");
        return transaction;
    }
}
