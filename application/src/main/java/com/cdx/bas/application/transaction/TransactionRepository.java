package com.cdx.bas.application.transaction;

import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionPersistencePort;
import com.cdx.bas.domain.transaction.TransactionStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/***
 * persistence implementation for Transaction entities
 * 
 * @author Clément Gibert
 *
 */
@ApplicationScoped
public class TransactionRepository implements TransactionPersistencePort, PanacheRepositoryBase<TransactionEntity, Long> {
    
    private static final Logger logger = Logger.getLogger(TransactionRepository.class);
    
    @Inject
    TransactionMapper transactionMapper;

    @Override
    public Optional<Transaction> findById(long id) {
        return findByIdOptional(id).map(transactionMapper::toDto);
    }

    @Override
    public Set<Transaction> getAll() {
        return findAll(Sort.by("status")).stream()
                .map(customer -> transactionMapper.toDto(customer))
                .collect(Collectors.toSet());
    }


    @Override
    public Set<Transaction> findAllByStatus(TransactionStatus transactionStatus) {
        return findAll(Sort.by("status")).stream()
                .filter(transaction -> transaction.getStatus().equals(transactionStatus))
                .map(customer -> transactionMapper.toDto(customer))
                .collect(Collectors.toSet());
    }

    @Override
    public Queue<Transaction> findUnprocessedTransactions() {
        return find("#TransactionEntity.findUnprocessed",
                Parameters.with("status", TransactionStatus.UNPROCESSED).map())
                .list()
                .stream().map(transactionMapper::toDto)
                .collect(Collectors.toCollection(PriorityQueue::new));
    }

    @Override
    @Transactional(TxType.REQUIRED)
    public Transaction create(Transaction transaction) {
        persist(transactionMapper.toEntity(transaction));
        logger.info("Transaction " + transaction.getId() + " created");
        return transaction;
    }

    @Override
    @Transactional(TxType.REQUIRED)
    public Transaction update(Transaction transaction) {
        getEntityManager().merge(transactionMapper.toEntity(transaction));
        logger.info("Transaction " + transaction.getId() + " updated");
        return transaction;
    }

    @Override
    @Transactional(TxType.REQUIRED)
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
