package com.cdx.bas.domain.bank.account;

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
     * @return List<BankAccount> corresponding to all the bank account
     */
    public BankAccount deposite(Long id, Long amount, String currency);
}
