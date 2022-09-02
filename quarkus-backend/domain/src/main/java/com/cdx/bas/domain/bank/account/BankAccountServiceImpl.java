package com.cdx.bas.domain.bank.account;

import javax.enterprise.context.RequestScoped;

import com.cdx.bas.domain.bank.money.Money;

@RequestScoped
public class BankAccountServiceImpl implements BankAccountService {
    
//    @Inject
//    BankAccountManager bankAccountManager;

    @Override
    public boolean deposit(long accountId, Money money) {
        // TODO Auto-generated method stub
        return false;
    }

}
