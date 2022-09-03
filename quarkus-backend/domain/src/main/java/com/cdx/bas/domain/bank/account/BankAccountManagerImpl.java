package com.cdx.bas.domain.bank.account;

import javax.enterprise.context.RequestScoped;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountManager;

@RequestScoped
public class BankAccountManagerImpl implements BankAccountManager {
    
    @Override
    public BankAccount findById(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void create(BankAccount bankAccount) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(BankAccount bankAccount) {
        // TODO Auto-generated method stub
        
    }

}
