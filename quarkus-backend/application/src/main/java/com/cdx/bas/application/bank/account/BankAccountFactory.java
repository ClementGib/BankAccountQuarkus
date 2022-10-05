package com.cdx.bas.application.bank.account;

import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.bank.account.mma.MMABankAccount;
import com.cdx.bas.domain.bank.account.saving.SavingBankAccount;

public class BankAccountFactory {
    public static BankAccount createBankAccount(AccountType type) {
        BankAccount bankAccount = null;
        if (AccountType.CHECKING.equals(type)) {
            bankAccount = new CheckingBankAccount();
        } else if (AccountType.SAVING.equals(type)) {
            bankAccount = new SavingBankAccount();
        } else if (AccountType.MMA.equals(type)) {
            bankAccount = new MMABankAccount();
        }
        return bankAccount;
    }
}