package com.cdx.bas.domain.bank.money;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MoneyTest {
    
    @Test
    public void isPositive_should_returnTrue_when_balanceValueIsGreaterToZero() {
        Money money = new Money();
        money.setAmount(BigDecimal.TEN);
        assertThat(money.isPositive()).isTrue();
    }
    
    @Test
    public void isPositive_should_returnFalse_when_balanceValueIsLessToZero() {
        Money money = new Money();
        money.setAmount(BigDecimal.TEN.negate());
        money.isPositive();
        assertThat(money.isPositive()).isFalse();
    }
    
    @Test
    public void isPositiveOrZero_should_returnTrue_when_balanceValueIsGreaterToZero() {
        Money money = new Money();
        money.setAmount(BigDecimal.TEN);
        assertThat(money.isPositiveOrZero()).isTrue();
    }
    
    @Test
    public void isPositiveOrZero_should_returnTrue_when_balanceValueIsEqualToZero() {
        Money money = new Money();
        money.setAmount(BigDecimal.TEN);
        assertThat(money.isPositiveOrZero()).isTrue();
    }
    
    @Test
    public void isPositiveOrZero_should_returnFalse_when_balanceValueIsLessToZero() {
        Money money = new Money();
        money.setAmount(BigDecimal.TEN.negate());
        money.isPositive();
        assertThat(money.isPositiveOrZero()).isFalse();
    }

    @Test
    public void isNegative_should_returnTrue_when_balanceValueIsLessToZero() {
        Money money = new Money();
        money.setAmount(BigDecimal.TEN.negate());
        assertThat(money.isNegative()).isTrue();
    }
    
    @Test
    public void isNegative_should_returnFalse_when_balanceValueIsGreaterToZero() {
        Money money = new Money();
        money.setAmount(BigDecimal.TEN);
        assertThat(money.isNegative()).isFalse();
    }
    
    @Test
    public void minus_should_subtractAmount_when_AmountIsPositiveAndValueIsPositive() {
        Money moneyOne = new Money();
        moneyOne.setAmount(BigDecimal.TEN);
        
        Money moneyTwo = new Money();
        moneyTwo.setAmount(BigDecimal.TEN);
        
        moneyOne.minus(moneyTwo);
        assertThat(moneyOne.getAmount()).isEqualTo(BigDecimal.ZERO);
    }
    
    @Test
    public void minus_should_additionAmount_when_ValuesIsNegative() {
        Money moneyOne = new Money();
        moneyOne.setAmount(BigDecimal.TEN.negate());
        
        Money moneyTwo = new Money();
        moneyTwo.setAmount(BigDecimal.TEN.negate());
        
        moneyOne.minus(moneyTwo);
        assertThat(moneyOne.getAmount()).isEqualTo(BigDecimal.ZERO);
    }
}
