package com.cdx.bas.application.scheduler;

import java.util.PriorityQueue;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionPersistencePort;
import com.cdx.bas.domain.transaction.TransactionServicePort;

import org.jboss.logging.Logger;

import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;

@Startup
@Singleton
public class SchedulerImpl implements Scheduler {

    private static final Logger logger = Logger.getLogger(SchedulerImpl.class);

    private static final PriorityQueue<Transaction> transactionQueue = new PriorityQueue<>();

    @Inject
    TransactionServicePort transactionService;

    @Inject
    TransactionPersistencePort transactionRepository;

    public PriorityQueue<Transaction> getTransactionQueue() {
        return transactionQueue;
    }

    @Scheduled(every = "5s")
    public void processQueue() {
        logger.info("Scheduler start");
        if (getTransactionQueue().isEmpty()) {
            getTransactionQueue().addAll(transactionRepository.findUnprocessedTransactions());
            logger.info("Queue size: " + transactionQueue.size());
            getTransactionQueue().forEach(transaction -> {
                transactionService.process(transaction);
            });
        }
        logger.info("Scheduler end");
    }
}