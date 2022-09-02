package com.cdx.bas.domain.bank.account;

import com.cdx.bas.domain.bank.money.Money;

public interface BankAccountService {

    public boolean deposit(long accountId, Money money);
}
