package com.cdx.bas.domain.bank.account;

import java.util.Optional;

public interface BankAccountManager {
    
    public Optional<BankAccount> findById(long id);
    
    public BankAccount create(BankAccount bankAccount);
    
    public BankAccount update(BankAccount bankAccount);
}
