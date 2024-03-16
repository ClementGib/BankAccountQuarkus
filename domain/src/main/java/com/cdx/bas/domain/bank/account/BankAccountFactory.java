package com.cdx.bas.domain.bank.account;

import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.bank.account.mma.MMABankAccount;
import com.cdx.bas.domain.bank.account.saving.SavingBankAccount;
import com.cdx.bas.domain.bank.account.type.AccountType;

public class BankAccountFactory {

    private BankAccountFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static BankAccount createBankAccount(AccountType type) {
        if (AccountType.CHECKING.equals(type)) {
            return new CheckingBankAccount();
        } else if (AccountType.SAVING.equals(type)) {
            return new SavingBankAccount();
        } else if (AccountType.MMA.equals(type)) {
            return new MMABankAccount();
        }
        throw new IllegalArgumentException("Unexpected account type " + type);
    }
}
