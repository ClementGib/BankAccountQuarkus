package com.cdx.bas.domain.bank.account;

import static org.assertj.core.api.Assertions.assertThat;

import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.bank.account.mma.MMABankAccount;
import com.cdx.bas.domain.bank.account.saving.SavingBankAccount;
import com.cdx.bas.domain.bank.account.type.AccountType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BankAccountFactoryTest {
    
    @Test
    public void createBankAccount_shouldReturnNull_whenAccountTypeIsNull() {
        // Act
        try {
            BankAccountFactory.createBankAccount(null);
        } catch (IllegalArgumentException exception) {
            assertThat(exception.getMessage()).isEqualTo("Unexpected account type null");
        }
    }

    @Test
    public void createBankAccount_shouldReturnCheckingBankAccountInstance_whenAccountTypeIsChecking() {
        // Act
        BankAccount bankAccount = BankAccountFactory.createBankAccount(AccountType.CHECKING);

        // Assert
        assertThat(bankAccount).isInstanceOf(CheckingBankAccount.class);
    }
    
    @Test
    public void createBankAccount_shouldReturnSavingBankAccountInstance_whenAccountTypeIsSaving() {
        // Act
        BankAccount bankAccount = BankAccountFactory.createBankAccount(AccountType.SAVING);

        // Assert
        assertThat(bankAccount).isInstanceOf(SavingBankAccount.class);
    }
    
    @Test
    public void createBankAccount_shouldReturnMMABankAccountInstance_whenAccountTypeIsMMA() {
        // Act
        BankAccount bankAccount = BankAccountFactory.createBankAccount(AccountType.MMA);

        // Assert
        assertThat(bankAccount).isInstanceOf(MMABankAccount.class);
    }
}