package com.cdx.bas.application.bank.account;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountPersistencePort;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 * persistence implementation for BankAccount entities
 * 
 * @author Clément Gibert
 *
 */
@RequestScoped
public class BankAccountRepository implements BankAccountPersistencePort, PanacheRepositoryBase<BankAccountEntity, Long> {
    
    private static final Logger logger = LoggerFactory.getLogger(BankAccountRepository.class);
    
    @Inject
    BankAccountMapper bankAccountMapper;

    @Override
    @Transactional
    public List<BankAccount> getAll() {
        return findAll(Sort.by("id")).stream()
                .map(bankAccountEntity -> bankAccountMapper.toDto(bankAccountEntity))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<BankAccount> findById(long id) {
        return findByIdOptional(id).map(bankAccountMapper::toDto);
    }
    
    @Override
    @Transactional
    public BankAccount create(BankAccount bankAccount) {
        getEntityManager().persist(bankAccountMapper.toEntity(bankAccount));
        logger.info("BankAccount " + bankAccount.getId() + " created");
        return bankAccount;
    }
    @Override
    @Transactional
    public BankAccount update(BankAccount bankAccount) {
        bankAccount = bankAccountMapper.toDto(getEntityManager().merge(bankAccountMapper.toEntity(bankAccount)));
        logger.info("BankAccount " + bankAccount.getId() + " updated");
        return bankAccount;
    }
    
    @Override
    @Transactional
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
