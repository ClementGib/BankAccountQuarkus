package com.cdx.bas.application.bank.account;

import com.cdx.bas.application.bank.transaction.TransactionTestUtils;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;
import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.bank.account.validation.BankAccountValidator;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionServicePort;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatusServicePort;
import com.cdx.bas.domain.money.Money;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.UNPROCESSED;
import static com.cdx.bas.domain.bank.transaction.type.TransactionType.CREDIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class BankAccountServiceImplTest {

    @InjectMock
    BankAccountPersistencePort bankAccountRepository;

    @InjectMock
    BankAccountValidator bankAccountValidator;

    @InjectMock
    TransactionServicePort transactionService;

    @Inject
    BankAccountServicePort bankAccountService;

    @Test
    public void findBankAccount_shouldFindBankAccount_whenBankAccountExists() {
        BankAccount bankAccount = BankAccountTestUtils.createBankAccountUtils(99L, Money.of(new BigDecimal("100")));

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(bankAccount));

        BankAccount actualBankAccount = bankAccountService.findBankAccount(1L);
        assertThat(actualBankAccount).isEqualTo(bankAccount);
        verify(bankAccountRepository).findById(1L);
        verifyNoMoreInteractions(bankAccountRepository);
        verifyNoInteractions(transactionService);
    }

    @Test
    public void findBankAccount_shouldReturnNull_whenBankAccountDoesNotExist() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        BankAccount actualBankAccount = bankAccountService.findBankAccount(1L);
        assertThat(actualBankAccount).isNull();
        verify(bankAccountRepository).findById(1L);
        verifyNoMoreInteractions(bankAccountRepository, bankAccountValidator);
        verifyNoInteractions(transactionService);
    }

    @Test
    public void addTransaction_shouldAddTransactionToBankAccount_whenTransactionDoesNotExist() {
        Instant timestamp = Instant.now();
        BankAccount bankAccount = BankAccountTestUtils.createBankAccountUtils(99L, Money.of(new BigDecimal("100")));
        Transaction transaction = TransactionTestUtils.createTransaction(10L, 99L, timestamp);


        BankAccount actualBankAccount = bankAccountService.addTransaction(transaction, bankAccount);
        assertThat(actualBankAccount.getIssuedTransactions().size()).isEqualTo(1);
        assertThat(actualBankAccount.getIssuedTransactions()).contains(transaction);

        verifyNoInteractions(transactionService, bankAccountValidator, bankAccountRepository);
    }

    @Test
    public void addTransaction_shouldUpdateTransactionToBankAccount_whenTransactionExists() {
        Instant timestamp = Instant.now();
        BankAccount bankAccount = BankAccountTestUtils.createBankAccountUtils(99L, Money.of(new BigDecimal("100")));
        Transaction transaction = TransactionTestUtils.createTransaction(10L, 99L, timestamp);
        bankAccount.getIssuedTransactions().add(transaction);

        when(transactionService.mergeTransactions(transaction, transaction)).thenReturn(transaction);

        BankAccount actualBankAccount = bankAccountService.addTransaction(transaction, bankAccount);
        assertThat(actualBankAccount.getIssuedTransactions().size()).isEqualTo(1);
        assertThat(actualBankAccount.getIssuedTransactions()).contains(transaction);

        verify(transactionService).mergeTransactions(transaction, transaction);
        verifyNoMoreInteractions(transactionService);
        verifyNoInteractions(bankAccountValidator, bankAccountRepository);
    }

    @Test
    public void updateBankAccount_shouldUpdateBankAccount_whenHasValidBankAccount() {
        BankAccount bankAccount = BankAccountTestUtils.createBankAccountUtils(99L, Money.of(new BigDecimal("100")));

        BankAccount actualBankAccount = bankAccountService.updateBankAccount(bankAccount);
        assertThat(actualBankAccount).isNull();
        verify(bankAccountValidator).validateBankAccount(bankAccount);
        verify(bankAccountRepository).update(bankAccount);
        verifyNoMoreInteractions(bankAccountValidator, bankAccountRepository);
        verifyNoInteractions(transactionService);
    }

    @Test
    public void transferAmountBetweenAccounts_shouldAddAmountOfMoneyCorrespondingToTransactionAmount_whenTransactionHasAmount() {
        Transaction transaction = TransactionTestUtils.createTransaction(10L, 99L, Instant.now());
        BankAccount bankAccountEmitter = BankAccountTestUtils.createBankAccountUtils(99L, Money.of(new BigDecimal("100")));
        BankAccount bankAccountReceiver = BankAccountTestUtils.createBankAccountUtils(100L, Money.of(new BigDecimal("0")));

        bankAccountService.transferAmountBetweenAccounts(transaction, bankAccountEmitter, bankAccountReceiver);

        assertThat(bankAccountEmitter.getBalance().getAmount()).isEqualTo(new BigDecimal("0"));
        assertThat(bankAccountReceiver.getBalance().getAmount()).isEqualTo(new BigDecimal("100"));
        verifyNoInteractions(transactionService, bankAccountValidator, bankAccountRepository);
    }
}
