package com.cdx.bas.application.manager;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountManager;

@RequestScoped
public class BankAccountManagerImpl implements BankAccountManager {
    
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

}
