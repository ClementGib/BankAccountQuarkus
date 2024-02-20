package com.cdx.bas.domain.bank.account;

import com.cdx.bas.domain.transaction.Transaction;

public interface BankAccountControllerPort {

    
    /**
     * find BankAccount from its id
     * 
     * @param id of BankAccount
     * @return BankAccount corresponding to the id
     */
    public BankAccount findById(long id);
    
    /**
     * make a deposite on bank account
     *
     * @param id of BankAccount
     * @param depositTransaction to add to the BankAccount
     * @return deposit Transaction added to the BankAccount
     */
    public Transaction deposite(Long id, Transaction depositTransaction);
}
