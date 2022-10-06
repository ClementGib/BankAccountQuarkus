package com.cdx.bas.domain.bank.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.bank.account.mma.MMABankAccount;
import com.cdx.bas.domain.bank.account.saving.SavingBankAccount;
import com.cdx.bas.domain.customer.Customer;
import com.cdx.bas.domain.customer.Gender;
import com.cdx.bas.domain.customer.MaritalStatus;
import com.cdx.bas.domain.money.Money;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BankAccountValidatorTest {

    @Inject
    BankAccountValidator bankAccountValidator;
    
    @Test
    public void validateBankAccount_should_doNothing_when_CheckingBankAccountIsValid(){
        BankAccount dto = new CheckingBankAccount();
        dto.setId(10L);
        dto.setType(AccountType.CHECKING);
        dto.setBalance(new Money(new BigDecimal("1000")));
        Set<Long> customers = new HashSet<>();
        Customer customer = createCustomer();
        customers.add(customer.getId());
        dto.setCustomersId(customers);
        
        bankAccountValidator.validateBankAccount(dto);
    }
    
    @Test
    public void validateBankAccount_should_doNothing_when_SavingBankAccountIsValid(){
        BankAccount dto = new SavingBankAccount();
        dto.setId(10L);
        dto.setType(AccountType.SAVING);
        dto.setBalance(new Money(new BigDecimal("1000")));
        Set<Long> customers = new HashSet<>();
        Customer customer = createCustomer();
        customers.add(customer.getId());
        dto.setCustomersId(customers);
        
        bankAccountValidator.validateBankAccount(dto);
    }
    
    @Test
    public void validateBankAccount_should_doNothing_when_MMABankAccountIsValid(){
        BankAccount dto = new MMABankAccount();
        dto.setId(10L);
        dto.setType(AccountType.MMA);
        dto.setBalance(new Money(new BigDecimal("1000")));
        Set<Long> customers = new HashSet<>();
        Customer customer = createCustomer();
        customers.add(customer.getId());
        dto.setCustomersId(customers);
        
        bankAccountValidator.validateBankAccount(dto);
    }
    
    @Test
    public void validateBankAccount_should_throwBankAccountException_when_CheckingBankAccountIsInvalid(){
        BankAccount dto = new CheckingBankAccount();
        dto.setId(10L);
        dto.setType(AccountType.CHECKING);
        dto.setBalance(new Money(new BigDecimal("100001")));
        Set<Long> customers = new HashSet<>();
        Customer customer = createCustomer();
        customers.add(customer.getId());
        dto.setCustomersId(customers);
        
        try {
            bankAccountValidator.validateBankAccount(dto);
            fail();
        } catch (BankAccountException exception) {
            assertThat(exception.getMessage()).hasToString("balance amount must be between -600 and 100000\n");
        }
    }
    
    @Test
    public void validateBankAccount_should_throwBankAccountException_when_SavingBankAccountIsInvalid(){
        BankAccount dto = new SavingBankAccount();
        dto.setId(10L);
        dto.setType(AccountType.SAVING);
        dto.setBalance(new Money(new BigDecimal("22951")));
        Set<Long> customers = new HashSet<>();
        Customer customer = createCustomer();
        customers.add(customer.getId());
        dto.setCustomersId(customers);
        
        try {
            bankAccountValidator.validateBankAccount(dto);
            fail();
        } catch (BankAccountException exception) {
            assertThat(exception.getMessage()).hasToString("balance amount must be between 1 and 22950\n");
        }
    }
    
    @Test
    public void validateBankAccount_should_throwBankAccountException_when_MMABankAccountIsInvalid(){
        BankAccount dto = new MMABankAccount();
        dto.setId(10L);
        dto.setType(AccountType.MMA);
        dto.setBalance(new Money(new BigDecimal("250001")));
        Set<Long> customers = new HashSet<>();
        Customer customer = createCustomer();
        customers.add(customer.getId());
        dto.setCustomersId(customers);
        
        try {
            bankAccountValidator.validateBankAccount(dto);
            fail();
        } catch (BankAccountException exception) {
            assertThat(exception.getMessage()).hasToString("balance amount must be between 1000 and 250000\n");
        }
    }
    
    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setId(99L);
        customer.setFirstName("Paul");
        customer.setLastName("Martin");
        customer.setGender(Gender.MALE);
        customer.setMaritalStatus(MaritalStatus.SINGLE);
        customer.setBirthdate(LocalDateTime.of(1995, Month.MAY, 3, 6, 30, 40, 50000));
        customer.setCountry("FR");
        customer.setAddress("100 avenue de la république");
        customer.setCity("Paris");
        customer.setEmail("jean.dupont@yahoo.fr");
        customer.setPhoneNumber("+33642645678");
        return customer;
    }
}
