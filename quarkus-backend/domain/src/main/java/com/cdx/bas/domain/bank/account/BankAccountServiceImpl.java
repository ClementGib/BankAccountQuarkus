package com.cdx.bas.domain.bank.account;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.domain.bank.money.Money;

@RequestScoped
public class BankAccountServiceImpl implements BankAccountService {
    
//    @Inject
//    BankAccountManager bankAccountManager;

    @Override
    public boolean deposit(Long accountId, Money money) {
        // TODO Auto-generated method stub
        return false;
    }

}
