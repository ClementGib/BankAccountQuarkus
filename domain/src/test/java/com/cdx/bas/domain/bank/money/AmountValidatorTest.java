package com.cdx.bas.domain.bank.money;

import com.cdx.bas.domain.money.Amount;
import com.cdx.bas.domain.money.Money;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class AmountValidatorTest {
    
    @Inject
    Validator validator;
    
    @Test
    public void isValid_shouldGeneratesConstraintValidationWithDefaultValue_whenBalanceNotValidAndMessageIsNotSpecified() {
        Money money = new Money(new BigDecimal("10001"));
        DefaultValidatorTester validatorTester = new DefaultValidatorTester();
        validatorTester.balance = money;
        
        
        Set<ConstraintViolation<DefaultValidatorTester>> violations = validator.validate(validatorTester);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).hasToString("balance amount must respect the bank account restrictions.");
    }
    
    @Test
    public void isValid_shouldGeneratesConstraintsValidation_whenAmountOfMoneyIsLowerThanMinValue() {
        Money money = new Money(new BigDecimal("-100"));
        SpecifiedValidatorTester validatorTester = new SpecifiedValidatorTester();
        validatorTester.balance = money;
        
        
        Set<ConstraintViolation<SpecifiedValidatorTester>> violations = validator.validate(validatorTester);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).hasToString("balance must be between 1 and 99999.");
    }
    
    @Test
    public void isValid_shouldGeneratesConstraintValidation_whenAmountOfMoneyIsGreaterThanMaxValue() {
        Money money = new Money(new BigDecimal("100001"));
        SpecifiedValidatorTester validatorTester = new SpecifiedValidatorTester();
        validatorTester.balance = money;
        
        
        Set<ConstraintViolation<SpecifiedValidatorTester>> violations = validator.validate(validatorTester);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).hasToString("balance must be between 1 and 99999.");
    }
    
    @Test
    public void isValid_shouldGenerateConstraintValidation_whenAmountOfMoneyIsBetweenMinAndMaxValues() {
        Money money = new Money(new BigDecimal("100"));
        SpecifiedValidatorTester validatorTester = new SpecifiedValidatorTester();
        validatorTester.balance = money;
        
        
        Set<ConstraintViolation<SpecifiedValidatorTester>> violations = validator.validate(validatorTester);
        assertThat(violations).isEmpty();
    }
    
    @Test
    public void isValid_shouldGenerateConstraintsValidationFromBothValidators_whenUsingTwoValidators() {
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
    
    private static class DefaultValidatorTester {
        @Amount(min=1, max=1000)
        protected Money balance;
    }
    
    private static class SpecifiedValidatorTester {
        @Amount(min=1, max=99999, message="balance must be between 1 and 99999.")
        protected Money balance;
    }
}
