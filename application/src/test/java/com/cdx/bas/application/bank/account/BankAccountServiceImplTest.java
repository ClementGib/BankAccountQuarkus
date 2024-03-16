package com.cdx.bas.application.bank.account;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;
import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.bank.account.validation.BankAccountValidator;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionServicePort;
import com.cdx.bas.domain.money.Money;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.cdx.bas.domain.bank.account.type.AccountType.CHECKING;
import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.ERROR;
import static com.cdx.bas.domain.bank.transaction.type.TransactionType.CREDIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        // Arrange
        BankAccount bankAccount = CheckingBankAccount.builder()
                .id(99L)
                .type(CHECKING)
                .balance(Money.of(new BigDecimal("100")))
                .customersId(List.of(99L))
                .build();

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(bankAccount));

        // Act
        BankAccount actualBankAccount = bankAccountService.findBankAccount(1L);

        // Assert
        assertThat(actualBankAccount).isEqualTo(bankAccount);
        verify(bankAccountRepository).findById(1L);
        verifyNoMoreInteractions(bankAccountRepository);
        verifyNoInteractions(transactionService);
    }

    @Test
    public void findBankAccount_shouldReturnNull_whenBankAccountDoesNotExist() {
        // Arrange
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        BankAccount actualBankAccount = bankAccountService.findBankAccount(1L);

        // Assert
        assertThat(actualBankAccount).isNull();
        verify(bankAccountRepository).findById(1L);
        verifyNoMoreInteractions(bankAccountRepository, bankAccountValidator);
        verifyNoInteractions(transactionService);
    }

    @Test
    public void addTransaction_shouldAddTransactionToBankAccount_whenTransactionDoesNotExist() {
        // Arrange
        Instant timestamp = Instant.now();
        BankAccount bankAccount = CheckingBankAccount.builder()
                .id(99L)
                .type(CHECKING)
                .balance(Money.of(new BigDecimal("100")))
                .customersId(List.of(99L))
                .issuedTransactions(new HashSet<>())
                .build();

        // Act
        Transaction transaction = Transaction.builder()
                .id(10L)
                .type(CREDIT)
                .emitterAccountId(99L)
                .receiverAccountId(77L)
                .amount(new BigDecimal("100"))
                .currency("EUR")
                .status(ERROR)
                .date(timestamp)
                .label("transaction test")
                .build();

        // Assert
        BankAccount actualBankAccount = bankAccountService.addTransaction(transaction, bankAccount);
        assertThat(actualBankAccount.getIssuedTransactions().size()).isEqualTo(1);
        assertThat(actualBankAccount.getIssuedTransactions()).contains(transaction);

        verifyNoInteractions(transactionService, bankAccountValidator, bankAccountRepository);
    }

    @Test
    public void addTransaction_shouldUpdateTransactionToBankAccount_whenTransactionExists() {
        // Arrange
        Instant timestamp = Instant.now();
        BankAccount bankAccount = CheckingBankAccount.builder()
                .id(99L)
                .type(CHECKING)
                .balance(Money.of(new BigDecimal("100")))
                .customersId(List.of(99L))
                .issuedTransactions(new HashSet<>())
                .build();
        Transaction transaction = Transaction.builder()
                .id(10L)
                .type(CREDIT)
                .emitterAccountId(99L)
                .receiverAccountId(77L)
                .amount(new BigDecimal("100"))
                .currency("EUR")
                .status(ERROR)
                .date(timestamp)
                .label("transaction test")
                .build();
        bankAccount.getIssuedTransactions().add(transaction);

        when(transactionService.mergeTransactions(transaction, transaction)).thenReturn(transaction);

        // Act
        BankAccount actualBankAccount = bankAccountService.addTransaction(transaction, bankAccount);

        // Assert
        assertThat(actualBankAccount.getIssuedTransactions().size()).isEqualTo(1);
        assertThat(actualBankAccount.getIssuedTransactions()).contains(transaction);
        verify(transactionService).mergeTransactions(transaction, transaction);
        verifyNoMoreInteractions(transactionService);
        verifyNoInteractions(bankAccountValidator, bankAccountRepository);
    }

    @Test
    public void updateBankAccount_shouldUpdateBankAccount_whenHasValidBankAccount() {
        // Arrange
        BankAccount bankAccount = CheckingBankAccount.builder()
                .id(99L)
                .type(CHECKING)
                .balance(Money.of(new BigDecimal("100")))
                .customersId(List.of(99L))
                .build();

        // Act
        BankAccount actualBankAccount = bankAccountService.updateBankAccount(bankAccount);

        // Assert
        assertThat(actualBankAccount).isNull();
        verify(bankAccountValidator).validateBankAccount(bankAccount);
        verify(bankAccountRepository).update(bankAccount);
        verifyNoMoreInteractions(bankAccountValidator, bankAccountRepository);
        verifyNoInteractions(transactionService);
    }

    @Test
    public void transferAmountBetweenAccounts_shouldAddAmountOfMoneyCorrespondingToTransactionAmount_whenTransactionHasAmount() {
        // Arrange
        Transaction transaction = Transaction.builder()
                .id(10L)
                .type(CREDIT)
                .emitterAccountId(99L)
                .receiverAccountId(77L)
                .amount(new BigDecimal("100"))
                .currency("EUR")
                .status(ERROR)
                .date(Instant.now())
                .label("transaction test")
                .build();
        BankAccount bankAccountEmitter = CheckingBankAccount.builder()
                .id(99L)
                .type(CHECKING)
                .balance(Money.of(new BigDecimal("100")))
                .customersId(List.of(99L))
                .build();
        BankAccount bankAccountReceiver = CheckingBankAccount.builder()
                .id(100L)
                .type(CHECKING)
                .balance(Money.of(new BigDecimal("0")))
                .customersId(List.of(99L))
                .build();

        // Act
        bankAccountService.transferAmountBetweenAccounts(transaction, bankAccountEmitter, bankAccountReceiver);

        // Assert
        assertThat(bankAccountEmitter.getBalance().getAmount()).isEqualTo(new BigDecimal("0"));
        assertThat(bankAccountReceiver.getBalance().getAmount()).isEqualTo(new BigDecimal("100"));
        verifyNoInteractions(transactionService, bankAccountValidator, bankAccountRepository);
    }
}
