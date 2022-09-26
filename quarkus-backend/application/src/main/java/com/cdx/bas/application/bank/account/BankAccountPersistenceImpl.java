package com.cdx.bas.application.bank.account;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;

@RequestScoped
public class BankAccountPersistenceImpl implements BankAccountPersistencePort {
    
    @Override
    public Optional<BankAccount> findById(long id) {
        return Optional.of(null);
    }
    
    @Override
    public BankAccount create(BankAccount bankAccount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<BankAccount> deleteById(long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

}
