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
}
