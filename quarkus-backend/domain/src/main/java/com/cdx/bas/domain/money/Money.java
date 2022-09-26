package com.cdx.bas.domain.money;

import java.math.BigDecimal;

import net.dv8tion.jda.api.MessageBuilder;

public class Money {
    
    private BigDecimal amount;
    
    public Money(BigDecimal amount) {
        this.amount = amount;
        validate();
    }

    private void validate() {
        MessageBuilder messageBuilder = new MessageBuilder();
        
        if (amount == null) {
            messageBuilder.append("amount must not be null.\n");
        }
        
        if (messageBuilder.length() > 0) {
            throw new IllegalStateException(messageBuilder.build().getContentRaw());
        }
    }
    
    public static Money of(long value) {
        return new Money(BigDecimal.valueOf(value));
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
