package com.cdx.bas.application.transaction;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionPersistencePort;
import com.cdx.bas.domain.transaction.TransactionStatus;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

/***
 * specific dao interface for transaction entities
 * 
 * @author Clément Gibert
 *
 */
@ApplicationScoped
public class TransactionRepository implements TransactionPersistencePort, PanacheRepositoryBase<TransactionEntity, Long> {
    
    private static final Logger logger = Logger.getLogger(TransactionRepository.class);
    
    @Inject
    private DtoEntityMapper<Transaction, TransactionEntity> transactionMapper;

    @Override
    public Optional<Transaction> findById(long id) {
        return findByIdOptional(id).map(transactionMapper::toDto);
    }

    @Override
    public Queue<Transaction> findUnprocessedTransactions() {
        return find("#TransactionEntity.findUnprocessed", Parameters.with("status", TransactionStatus.WAITING).map())
                .list()
                .stream().map(transactionMapper::toDto)
                .collect(Collectors.toCollection(PriorityQueue::new));
    }

    @Override
    public Transaction create(Transaction transaction) {
        persist(transactionMapper.toEntity(transaction));
        logger.info("Transaction " + transaction.getId() + " created");
        return transaction;
    }

    @Override
    public Transaction update(Transaction transaction) {
        persist(transactionMapper.toEntity(transaction));
        logger.info("Transaction " + transaction.getId() + " updated");
        return transaction;
    }

    @Override
    public Optional<Transaction> deleteById(long id) {
        Optional<TransactionEntity> entityOptional = findByIdOptional(id);
        if (entityOptional.isPresent()) {
            TransactionEntity entity = entityOptional.get();
            delete(entity);
            logger.info("Transaction " + entity.getId() + " deleted");
            return Optional.of(transactionMapper.toDto(entity));
        }
        return Optional.empty();
    }


}
