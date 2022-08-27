package com.cdx.bas.domain.bank.account;

import javax.inject.Inject;

import com.cdx.bas.domain.bank.money.Money;

public class BankAccountServiceImpl implements BankAccountService {
    
    @Inject
    BankAccountManager bankAccountManager;

    @Override
    public boolean deposit(Money money, Long accountId) {
        // TODO Auto-generated method stub
        return false;
    }

}
