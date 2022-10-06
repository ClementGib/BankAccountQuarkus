package com.cdx.bas.application.bank.account;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountException;
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
    public BankAccount create(BankAccount bankAccount) throws BankAccountException {
        persistAndFlush(bankAccountMapper.toEntity(bankAccount));
        return bankAccount;
    }

    @Override
    public BankAccount update(BankAccount bankAccount) throws BankAccountException {
        persistAndFlush(bankAccountMapper.toEntity(bankAccount));
        return bankAccount;
    }

    @Override
    public Optional<BankAccount> deleteById(long id) {
        Optional<BankAccountEntity> entityOptional = findByIdOptional(id);
        if (entityOptional.isPresent()) {
            BankAccountEntity entity = entityOptional.get();
            delete(entity);
            return Optional.of(bankAccountMapper.toDto(entity));
        }
        return Optional.empty();
    }
}
