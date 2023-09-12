package com.cdx.bas.application.bank.account;

import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

/***
 * persistence implementation for BankAccount entities
 * 
 * @author Clément Gibert
 *
 */
@ApplicationScoped
public class BankAccountRepository implements BankAccountPersistencePort, PanacheRepositoryBase<BankAccountEntity, Long> {
    
    private static final Logger logger = LoggerFactory.getLogger(BankAccountRepository.class);
    
    @Inject
    private DtoEntityMapper<BankAccount, BankAccountEntity> bankAccountMapper;
    
    @Override
    public Optional<BankAccount> findById(long id) {
        return findByIdOptional(id).map(bankAccountMapper::toDto);
    }
    
    @Override
    public BankAccount create(BankAccount bankAccount) {
        persist(bankAccountMapper.toEntity(bankAccount));
        logger.info("BankAccount " + bankAccount.getId() + " created");
        return bankAccount;
    }

    @Override
    @Transactional(value = Transactional.TxType.MANDATORY)
    public BankAccount update(BankAccount bankAccount) {
        getEntityManager().merge(bankAccountMapper.toEntity(bankAccount));
        logger.info("BankAccount " + bankAccount.getId() + " updated");
        return bankAccount;
    }
    
    @Override
    public Optional<BankAccount> deleteById(long id) {
        Optional<BankAccountEntity> entityOptional = findByIdOptional(id);
        if (entityOptional.isPresent()) {
            BankAccountEntity entity = entityOptional.get();
            delete(entity);
            logger.info("BankAccount " + entity.getId() + " deleted");
            return Optional.of(bankAccountMapper.toDto(entity));
        }
        return Optional.empty();
    }
}
