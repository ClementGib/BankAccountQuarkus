package com.cdx.bas.application.transaction;

import java.util.List;

import com.cdx.bas.application.repository.GenericRepository;

public interface TransactionRepository extends GenericRepository<TransactionEntity, Long> {

    /**
     * Find all existing transactions entities
     * 
     * 
     * @return all the transactions
     */
    List<TransactionEntity> findAll();
}
