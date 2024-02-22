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
        BankAccount bankAccount = BankAccountFactory.createBankAccount(null);
        
        assertThat(bankAccount).isNull();
    }

    @Test
    public void createBankAccount_shouldReturnCheckingBankAccountInstance_whenAccountTypeIsChecking() {
        BankAccount bankAccount = BankAccountFactory.createBankAccount(AccountType.CHECKING);
        
        assertThat(bankAccount).isInstanceOf(CheckingBankAccount.class);
    }
    
    @Test
    public void createBankAccount_shouldReturnSavingBankAccountInstance_whenAccountTypeIsSaving() {
        BankAccount bankAccount = BankAccountFactory.createBankAccount(AccountType.SAVING);
        
        assertThat(bankAccount).isInstanceOf(SavingBankAccount.class);
    }
    
    @Test
    public void createBankAccount_shouldReturnMMABankAccountInstance_whenAccountTypeIsMMA() {
        BankAccount bankAccount = BankAccountFactory.createBankAccount(AccountType.MMA);
        
        assertThat(bankAccount).isInstanceOf(MMABankAccount.class);
    }
}