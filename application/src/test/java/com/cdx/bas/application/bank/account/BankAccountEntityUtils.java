package com.cdx.bas.application.bank.account;

import com.cdx.bas.application.bank.customer.CustomerEntity;
import com.cdx.bas.domain.bank.account.type.AccountType;

import java.math.BigDecimal;
import java.util.*;

public class BankAccountEntityUtils {

    public static BankAccountEntity createBankAccountEntity(long id) {
        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setId(id);
        bankAccountEntity.setType(AccountType.CHECKING);
        bankAccountEntity.setBalance(new BigDecimal("100"));
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(99L);
        bankAccountEntity.setCustomers(List.of(customerEntity));
        return bankAccountEntity;
    }
}
