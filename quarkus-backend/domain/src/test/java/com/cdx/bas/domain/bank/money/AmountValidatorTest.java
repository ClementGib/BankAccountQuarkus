package com.cdx.bas.domain.bank.money;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.cdx.bas.domain.money.Amount;
import com.cdx.bas.domain.money.Money;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AmountValidatorTest {
    
    @Inject
    Validator validator;
    
    @Test
    public void isValid_should_generatesConstraintValidationWithDefaultValue_when_balanceNotValidAndMessageIsNotSpecified() {
        Money money = new Money(new BigDecimal("10001"));
        DefaultValidatorTester validatorTester = new DefaultValidatorTester();
        validatorTester.balance = money;
        
        
        Set<ConstraintViolation<DefaultValidatorTester>> violations = validator.validate(validatorTester);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).hasToString("balance amount must respect the bank account restrictions.");
    }
    
    @Test
    public void isValid_should_generatesConstraintsValidation_when_amountOfMoneyIsLowerThanMinValue() {
        Money money = new Money(new BigDecimal("-100"));
        SpecifiedValidatorTester validatorTester = new SpecifiedValidatorTester();
        validatorTester.balance = money;
        
        
        Set<ConstraintViolation<SpecifiedValidatorTester>> violations = validator.validate(validatorTester);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).hasToString("balance must be between 1 and 99999.");
    }
    
    @Test
    public void isValid_should_generatesConstraintValidation_when_amountOfMoneyIsGreaterThanMaxValue() {
        Money money = new Money(new BigDecimal("100001"));
        SpecifiedValidatorTester validatorTester = new SpecifiedValidatorTester();
        validatorTester.balance = money;
        
        
        Set<ConstraintViolation<SpecifiedValidatorTester>> violations = validator.validate(validatorTester);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).hasToString("balance must be between 1 and 99999.");
    }
    
    @Test
    public void isValid_should_generateConstraintValidation_when_amountOfMoneyIsBetweenMinAndMaxValues() {
        Money money = new Money(new BigDecimal("100"));
        SpecifiedValidatorTester validatorTester = new SpecifiedValidatorTester();
        validatorTester.balance = money;
        
        
        Set<ConstraintViolation<SpecifiedValidatorTester>> violations = validator.validate(validatorTester);
        assertThat(violations).isEmpty();
    }
    
    @Test
    public void isValid_should_generateConstraintsValidationFromBothValidators_when_usingTwoValidators() {
        Money money = new Money(new BigDecimal("100001"));
        DefaultValidatorTester defaultValidatorTester = new DefaultValidatorTester();
        defaultValidatorTester.balance = money;
        SpecifiedValidatorTester specifiedValidatorTester = new SpecifiedValidatorTester();
        specifiedValidatorTester.balance = money;
        
        Set<ConstraintViolation<DefaultValidatorTester>> defaultViolations = validator.validate(defaultValidatorTester);
        Set<ConstraintViolation<SpecifiedValidatorTester>> specifiedViolations = validator.validate(specifiedValidatorTester);
        assertThat(defaultViolations.iterator().next().getMessage()).hasToString("balance amount must respect the bank account restrictions.");
        assertThat(specifiedViolations.iterator().next().getMessage()).hasToString("balance must be between 1 and 99999.");
    }
    
    private class DefaultValidatorTester {
        @Amount(min=1, max=1000)
        protected Money balance;
    }
    
    private class SpecifiedValidatorTester {
        @Amount(min=1, max=99999, message="balance must be between 1 and 99999.")
        protected Money balance;
    }
}
