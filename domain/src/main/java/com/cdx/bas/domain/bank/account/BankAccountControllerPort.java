package com.cdx.bas.domain.bank.account;

import java.util.Set;

public interface BankAccountControllerPort {

    /**
     * Find all bank accounts
     *
     * @return all BankAccount found
     */
    public Set<BankAccount> getAll();
    
    /**
     * Find BankAccount from its id
     * 
     * @param id of BankAccount
     * @return BankAccount corresponding to the id
     */
    public BankAccount findById(long id);
}
