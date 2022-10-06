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

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

/***
 * specific dao interface for transaction entities
 * 
 * @author Clément Gibert
 *
 */
@ApplicationScoped
public class TransactionRepository implements TransactionPersistencePort, PanacheRepository<TransactionEntity> {
    
    private static final Logger logger = Logger.getLogger(TransactionRepository.class);
    
    @Inject
    private DtoEntityMapper<Transaction, TransactionEntity> transactionMapper;

    @Override
    public Optional<Transaction> findById(long id) {
        return findByIdOptional(id).map(transactionMapper::toDto);
    }

    @Override
    public Queue<Transaction> findUnprocessedTransactions() {
        Queue<Transaction> test = find("#TransactionEntity.findUnprocessed", Parameters.with("status", TransactionStatus.WAITING).map())
                .list()
                .stream().map(transactionMapper::toDto)
                .collect(Collectors.toCollection(PriorityQueue::new));
        test.forEach(elem -> logger.info(elem.getLabel()));
        return new PriorityQueue<>();
    }

    @Override
    public Transaction create(Transaction transaction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Transaction update(Transaction transaction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(Transaction transaction) {
        // TODO Auto-generated method stub
        
    }
    //extends TransactionPersistencePort , <TransactionEntity>
}
