package com.cdx.bas.domain.bank.money;

import java.math.BigDecimal;

public class Money {
    
    private BigDecimal amount;
    
    public Money(BigDecimal amount) {
        if (amount == null) {
            throw new NumberFormatException("Money amount value cannot be null.");
        }
        this.amount = amount;
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
}
