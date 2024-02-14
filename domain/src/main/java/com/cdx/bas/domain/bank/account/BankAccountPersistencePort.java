package com.cdx.bas.domain.bank.account;

import java.util.Optional;

public interface BankAccountPersistencePort {
    
    /**
     * find BankAccount from its id
     * 
     * @param id of BankAccount
     * @return <Optional>BankAccount if id corresponding or not to a BankAccount
     */
    public Optional<BankAccount> findById(long id);
    
    /**
     * create the current BankAccount
     * 
     * @param BankAccount to create
     * @return created BankAccount
     */
    public BankAccount create(BankAccount bankAccount);
    
    /**
     * update the current BankAccount
     * 
     * @param BankAccount to update
     * @return updated BankAccount
     */
    public BankAccount update(BankAccount bankAccount);
    
    /**
     * delete BankAccount from its id
     * 
     * @param id of BankAccount
     * @return BankAccount if id corresponding or not to a BankAccount
     */
    public Optional<BankAccount> deleteById(long id);
}
