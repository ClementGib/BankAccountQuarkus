package com.cdx.bas.application.bank.account;

import com.cdx.bas.application.bank.customer.CustomerEntity;
import com.cdx.bas.domain.bank.account.type.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class BankAccountTestUtils {

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

    public static BankAccount createBankAccountUtils(long accountId, Money amountOfMoney, Transaction...transactions) {
        BankAccount bankAccount = new CheckingBankAccount();
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(amountOfMoney);
        List<Long> customersId = new ArrayList<>();
        customersId.add(99L);
        bankAccount.setCustomersId(customersId);
        HashSet<Transaction> transactionHistory = new HashSet<>();
        Collections.addAll(transactionHistory, transactions);
        bankAccount.setIssuedTransactions(transactionHistory);
        return bankAccount;
    }

    public static BankAccount createBankAccountUtils(long accountId, String amount, Transaction...transactions) {
        BankAccount bankAccount = new CheckingBankAccount();
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(new Money(new BigDecimal(amount)));
        List<Long> customersId = new ArrayList<>();
        customersId.add(99L);
        bankAccount.setCustomersId(customersId);
        HashSet<Transaction> transactionHistory = new HashSet<>();
        Collections.addAll(transactionHistory, transactions);
        bankAccount.setIssuedTransactions(transactionHistory);
        return bankAccount;
    }
}
