package com.cdx.bas.domain.bank.account;

import static org.assertj.core.api.Assertions.assertThat;

import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.bank.account.mma.MMABankAccount;
import com.cdx.bas.domain.bank.account.saving.SavingBankAccount;
import com.cdx.bas.domain.utils.BankAccountFactory;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BankAccountFactoryTest {
    
    @Test
    public void createBankAccount_should_returnNull_when_AccountTypeIsNull() {
        BankAccount bankAccount = BankAccountFactory.createBankAccount(null);
        
        assertThat(bankAccount).isNull();
    }

    @Test
    public void createBankAccount_should_returnCheckingBankAccountInstance_when_AccountTypeIsChecking() {
        BankAccount bankAccount = BankAccountFactory.createBankAccount(AccountType.CHECKING);
        
        assertThat(bankAccount).isInstanceOf(CheckingBankAccount.class);
    }
    
    @Test
    public void createBankAccount_should_returnSavingBankAccountInstance_when_AccountTypeIsSaving() {
        BankAccount bankAccount = BankAccountFactory.createBankAccount(AccountType.SAVING);
        
        assertThat(bankAccount).isInstanceOf(SavingBankAccount.class);
    }
    
    @Test
    public void createBankAccount_should_returnMMABankAccountInstance_when_AccountTypeIsMMA() {
        BankAccount bankAccount = BankAccountFactory.createBankAccount(AccountType.MMA);
        
        assertThat(bankAccount).isInstanceOf(MMABankAccount.class);
    }
}
