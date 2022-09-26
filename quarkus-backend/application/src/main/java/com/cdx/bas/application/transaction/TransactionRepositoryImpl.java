package com.cdx.bas.application.transaction;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.cdx.bas.application.repository.JPARepository;

@Transactional(value = TxType.MANDATORY)
public class TransactionRepositoryImpl extends JPARepository<TransactionEntity, Long> implements TransactionRepository {
    @Override
    public List<TransactionEntity> findAll() {
        return this.getEntityManager()
                .createNamedQuery("TransactionEntity.findAll", TransactionEntity.class)
                .getResultList();
    }

}
