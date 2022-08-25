package com.cdx.bas.domain.bank.money;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MoneyTest {
    
    @Test
    public void minus_should_subtractAmount_when_AmountIsPositiveAndValueIsPositive() {
        Money moneyOne = new Money(new BigDecimal("100"));
        Money moneyTwo = new Money(new BigDecimal("100"));
        
        moneyOne.minus(moneyTwo);
        assertThat(moneyOne.getAmount()).isEqualTo(BigDecimal.ZERO);
    }
    
    @Test
    public void minus_should_subtractAmount_when_AmountIsZeroAndValueIsPositive() {
        Money moneyOne = new Money(new BigDecimal("0"));
        Money moneyTwo = new Money(new BigDecimal("100"));
        
        moneyOne.minus(moneyTwo);
        assertThat(moneyOne.getAmount()).isEqualTo(new BigDecimal("-100"));
    }
    
    @Test
    public void minus_should_additionAmount_when_ValuesIsNegative() {
        Money moneyOne = new Money(new BigDecimal("-100"));
        Money moneyTwo = new Money(new BigDecimal("-100"));
        
        moneyOne.minus(moneyTwo);
        assertThat(moneyOne.getAmount()).isEqualTo(BigDecimal.ZERO);
    }
    
    @Test
    public void plus_should_additionAmount_when_AmountIsPositiveAndValueIsPositive() {
        Money moneyOne = new Money(new BigDecimal("100"));
        Money moneyTwo = new Money(new BigDecimal("100"));
        
        moneyOne.plus(moneyTwo);
        assertThat(moneyOne.getAmount()).isEqualTo(new BigDecimal("200"));
    }
    
    @Test
    public void plus_should_additionAmount_when_AmountIsZeroAndValueIsPositive() {
        Money moneyOne = new Money(new BigDecimal("0"));
        Money moneyTwo = new Money(new BigDecimal("100"));
        
        moneyOne.plus(moneyTwo);
        assertThat(moneyOne.getAmount()).isEqualTo(new BigDecimal("100"));
    }
    
    @Test
    public void plus_should_additionAmount_when_ValuesIsNegative() {
        Money moneyOne = new Money(new BigDecimal("-100"));
        Money moneyTwo = new Money(new BigDecimal("-100"));
        
        moneyOne.plus(moneyTwo);
        assertThat(moneyOne.getAmount()).isEqualTo(new BigDecimal("-200"));
    }
    
    @Test
    public void isPositive_should_returnTrue_when_balanceValueIsGreaterToZero() {
        Money money = new Money(new BigDecimal("100"));
        assertThat(money.isPositive()).isTrue();
    }
    
    @Test
    public void isPositive_should_returnFalse_when_balanceValueIsLessToZero() {
        Money money = new Money(new BigDecimal("-100"));
        money.isPositive();
        assertThat(money.isPositive()).isFalse();
    }
    
    @Test
    public void isPositiveOrZero_should_returnTrue_when_balanceValueIsGreaterToZero() {
        Money money = new Money(new BigDecimal("100"));
        assertThat(money.isPositiveOrZero()).isTrue();
    }
    
    @Test
    public void isPositiveOrZero_should_returnTrue_when_balanceValueIsEqualToZero() {
        Money money = new Money(new BigDecimal("0"));
        assertThat(money.isPositiveOrZero()).isTrue();
    }
    
    @Test
    public void isPositiveOrZero_should_returnFalse_when_balanceValueIsLessToZero() {
        Money money = new Money(new BigDecimal("-100"));
        money.isPositive();
        assertThat(money.isPositiveOrZero()).isFalse();
    }

    @Test
    public void isNegative_should_returnTrue_when_balanceValueIsLessToZero() {
        Money money = new Money(new BigDecimal("-100"));
        assertThat(money.isNegative()).isTrue();
    }
    
    @Test
    public void isNegative_should_returnFalse_when_balanceValueIsGreaterToZero() {
        Money money = new Money(new BigDecimal("100"));
        assertThat(money.isNegative()).isFalse();
    }
}
