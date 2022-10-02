package com.cdx.bas.domain.bank.money;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import com.cdx.bas.domain.money.Money;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MoneyTest {

    @Test
    public void Money_should_instantiateMoneyWithAmountValue_when_amountValueIsAPositiveNumber() {
        Money money = new Money(new BigDecimal("100"));

        assertThat(money.getAmount()).isEqualTo("100");
    }

    @Test
    public void Money_should_instantiateMoneyWithAmountValue_when_amountValueIsANegativeNumber() {
        Money money = new Money(new BigDecimal("-100"));

        assertThat(money.getAmount()).isEqualTo("-100");
    }
    
    @Test
    public void of_should_returnMoneyWithAmountOfValue_when_ValueIsAPositiveNumber() {
        Money money = Money.of(100L);

        assertThat(money.getAmount()).isEqualTo(new BigDecimal("100"));
    }
    
    @Test
    public void of_should_returnMoneyWithAmountOfValue_when_ValueIsANegativeNumber() {
        Money money = Money.of(-100L);

        assertThat(money.getAmount()).isEqualTo(new BigDecimal("-100"));
    }

    @Test
    public void minus_should_subtractAmount_when_amountIsPositiveAndValueIsPositive() {
        Money moneyOne = new Money(new BigDecimal("100"));
        Money moneyTwo = new Money(new BigDecimal("100"));

        moneyOne.minus(moneyTwo);
        assertThat(moneyOne.getAmount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void minus_should_subtractAmount_when_amountIsZeroAndValueIsPositive() {
        Money moneyOne = new Money(new BigDecimal("0"));
        Money moneyTwo = new Money(new BigDecimal("100"));

        moneyOne.minus(moneyTwo);

        assertThat(moneyOne.getAmount()).isEqualTo(new BigDecimal("-100"));
    }

    @Test
    public void minus_should_additionAmount_when_valuesIsNegative() {
        Money moneyOne = new Money(new BigDecimal("-100"));
        Money moneyTwo = new Money(new BigDecimal("-100"));

        moneyOne.minus(moneyTwo);

        assertThat(moneyOne.getAmount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void plus_should_additionAmount_when_amountIsPositiveAndValueIsPositive() {
        Money moneyOne = new Money(new BigDecimal("100"));
        Money moneyTwo = new Money(new BigDecimal("100"));

        moneyOne.plus(moneyTwo);

        assertThat(moneyOne.getAmount()).isEqualTo(new BigDecimal("200"));
    }

    @Test
    public void plus_should_additionAmount_when_amountIsZeroAndValueIsPositive() {
        Money moneyOne = new Money(new BigDecimal("0"));
        Money moneyTwo = new Money(new BigDecimal("100"));

        moneyOne.plus(moneyTwo);

        assertThat(moneyOne.getAmount()).isEqualTo(new BigDecimal("100"));
    }

    @Test
    public void plus_should_additionAmount_when_valuesIsNegative() {
        Money moneyOne = new Money(new BigDecimal("-100"));
        Money moneyTwo = new Money(new BigDecimal("-100"));

        moneyOne.plus(moneyTwo);

        assertThat(moneyOne.getAmount()).isEqualTo(new BigDecimal("-200"));
    }

    @Test
    public void isPositive_should_returnTrue_when_amountValueIsGreaterToZero() {
        Money money = new Money(new BigDecimal("100"));
        assertThat(money.isPositive()).isTrue();
    }

    @Test
    public void isPositive_should_returnFalse_when_amountValueIsLessToZero() {
        Money money = new Money(new BigDecimal("-100"));

        money.isPositive();

        assertThat(money.isPositive()).isFalse();
    }

    @Test
    public void isPositiveOrZero_should_returnTrue_when_amountValueIsGreaterToZero() {
        Money money = new Money(new BigDecimal("100"));

        assertThat(money.isPositiveOrZero()).isTrue();
    }

    @Test
    public void isPositiveOrZero_should_returnTrue_when_amountValueIsEqualToZero() {
        Money money = new Money(new BigDecimal("0"));

        assertThat(money.isPositiveOrZero()).isTrue();
    }

    @Test
    public void isPositiveOrZero_should_returnFalse_when_amountValueIsLessToZero() {
        Money money = new Money(new BigDecimal("-100"));

        money.isPositive();

        assertThat(money.isPositiveOrZero()).isFalse();
    }

    @Test
    public void isNegative_should_returnTrue_when_amountValueIsLessToZero() {
        Money money = new Money(new BigDecimal("-100"));

        assertThat(money.isNegative()).isTrue();
    }

    @Test
    public void isNegative_should_returnFalse_when_amountValueIsGreaterToZero() {
        Money money = new Money(new BigDecimal("100"));

        assertThat(money.isNegative()).isFalse();
    }

    @Test
    public void isGreaterThan_should_returnTrue_when_amountValueIsGreaterThanAmountParameter() {
        Money moneyOne = new Money(new BigDecimal("100"));
        Money moneyTwo = new Money(new BigDecimal("50"));

        assertThat(moneyOne.isGreaterThan(moneyTwo)).isTrue();
    }

    @Test
    public void isGreaterThan_should_returnFalse_when_amountValueIsLessThanAmountParameter() {
        Money moneyOne = new Money(new BigDecimal("50"));
        Money moneyTwo = new Money(new BigDecimal("100"));

        assertThat(moneyOne.isGreaterThan(moneyTwo)).isFalse();
    }

    @Test
    public void isGreaterThan_should_returnFalse_when_amountValueIsEqualToAmountParameter() {
        Money moneyOne = new Money(new BigDecimal("100"));
        Money moneyTwo = new Money(new BigDecimal("100"));

        assertThat(moneyOne.isGreaterThan(moneyTwo)).isFalse();
    }

    @Test
    public void isGreaterThanOrEqual_should_returnTrue_when_amountValueIsGreaterThanAmountParameter() {
        Money moneyOne = new Money(new BigDecimal("100"));
        Money moneyTwo = new Money(new BigDecimal("50"));

        assertThat(moneyOne.isGreaterThanOrEqual(moneyTwo)).isTrue();
    }

    @Test
    public void isGreaterThanOrEqual_should_returnFalse_when_amountValueIsLessThanAmountParameter() {
        Money moneyOne = new Money(new BigDecimal("50"));
        Money moneyTwo = new Money(new BigDecimal("100"));

        assertThat(moneyOne.isGreaterThanOrEqual(moneyTwo)).isFalse();
    }

    @Test
    public void isGreaterThanOrEqual_should_returnTrue_when_amountValueIsEqualToAmountParameter() {
        Money moneyOne = new Money(new BigDecimal("100"));
        Money moneyTwo = new Money(new BigDecimal("100"));

        assertThat(moneyOne.isGreaterThanOrEqual(moneyTwo)).isTrue();
    }
}
