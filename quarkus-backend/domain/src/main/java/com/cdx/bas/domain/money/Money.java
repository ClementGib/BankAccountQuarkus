package com.cdx.bas.domain.money;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;

public class Money {
    @NotNull(message="amount must not be null.")
    private BigDecimal amount;
    
    public Money(BigDecimal amount) {
        this.amount = amount;
    }
    
    public static Money of(BigDecimal value) {
        return new Money(value);
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void minus(Money money) {
        this.amount = this.amount.subtract(money.amount);
    }
    
    public void plus(Money money) {
        this.amount = this.amount.add(money.amount);
    }
    
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public boolean isPositiveOrZero(){
        return this.amount.compareTo(BigDecimal.ZERO) >= 0;
    }
    
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
    
    public boolean isGreaterThan(Money money){
        return this.amount.compareTo(money.amount) >= 1;
    }
    
    public boolean isGreaterThanOrEqual(Money money){
        return this.amount.compareTo(money.amount) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
