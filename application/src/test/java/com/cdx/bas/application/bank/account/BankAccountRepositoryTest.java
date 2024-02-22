package com.cdx.bas.application.bank.account;

import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.application.bank.transaction.TransactionTestUtils;
import com.cdx.bas.domain.bank.account.type.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class BankAccountRepositoryTest {

    @Inject
    BankAccountRepository bankAccountRepository;
    
    @InjectMock
    DtoEntityMapper<BankAccount, BankAccountEntity> bankAccountMapper;


    @Test
    public void findById_shouldReturnBankAccount_whenAccountIsFound() {
        long accountId = 1L;
        Instant date = Instant.now();
        BankAccount bankAccount = createBankAccountUtils(accountId, date);

        when(bankAccountMapper.toDto(any())).thenReturn(bankAccount);
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(accountId);
        
        assertThat(optionalBankAccount).contains(bankAccount);
        verify(bankAccountMapper).toDto(any(BankAccountEntity.class));
        verifyNoMoreInteractions(bankAccountMapper);
    }
    
    @Test
    public void findById_shouldReturnEmptyOptional_whenAccountIsNotFound() {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(99999L);
        
        assertThat(optionalBankAccount).isEmpty();
        verifyNoInteractions(bankAccountMapper);
    }
    
    private BankAccount createBankAccountUtils(long accountId, Instant instantDate) {
        BankAccount bankAccount = new CheckingBankAccount();
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(new Money(new BigDecimal("300")));
        List<Long> customersId = new ArrayList<>();
        customersId.add(1L);
        bankAccount.setCustomersId(customersId);
        HashSet<Transaction> transactionHistory = new HashSet<>();
        transactionHistory.add(TransactionTestUtils.createTransactionUtils(accountId, instantDate));
        bankAccount.setIssuedTransactions(transactionHistory);
        return bankAccount;
    }
}
