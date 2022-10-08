package com.cdx.bas.application.scheduler;

import java.util.PriorityQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    private static PriorityQueue<Transaction> currentQueue = new PriorityQueue<>();

    @Inject
    TransactionServicePort transactionService;

    @Inject
    TransactionPersistencePort transactionRepository;

    public PriorityQueue<Transaction> getCurrentQueue() {
        return currentQueue;
    }

    @Scheduled(every = "5s")
    public void processQueue() {
        logger.info("Scheduler start");
        if (getCurrentQueue().isEmpty()) {
            getCurrentQueue().addAll(transactionRepository.findUnprocessedTransactions());
            logger.info("Queue size: " + currentQueue.size());
            getCurrentQueue().forEach(transaction -> {
                transactionService.processTransaction(transaction);
            });
        }
        logger.info("Scheduler end");
    }
}