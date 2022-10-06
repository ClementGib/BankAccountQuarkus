package com.cdx.bas.application.bank.account;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@RequestScoped
public class BankAccountRepository implements BankAccountPersistencePort, PanacheRepository<BankAccountEntity> {
    
    @Inject
    private DtoEntityMapper<BankAccount, BankAccountEntity> bankAccountMapper;
    
    @Override
    public Optional<BankAccount> findById(long id) {
        return findByIdOptional(id).map(bankAccountMapper::toDto);
    }
    
    @Override
    public BankAccount create(BankAccount bankAccount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<BankAccount> deleteById(long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }
}
