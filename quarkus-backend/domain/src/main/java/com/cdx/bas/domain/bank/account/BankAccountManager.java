package com.cdx.bas.domain.bank.account;

public interface BankAccountManager {
    
    public BankAccount findById(long id);
    
    public void create(BankAccount bankAccount);
    
    public void update(BankAccount bankAccount);
}
