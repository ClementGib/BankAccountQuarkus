package com.cdx.bas.application.scheduler;

import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionPersistencePort;
import com.cdx.bas.domain.transaction.TransactionServicePort;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.PriorityQueue;

@Startup
@Singleton
public class SchedulerImpl implements Scheduler {

    private static final Logger logger = Logger.getLogger(SchedulerImpl.class);

    private static final PriorityQueue<Transaction> transactionQueue = new PriorityQueue<>();

    @Inject
    TransactionServicePort transactionService;

    @Inject
    TransactionPersistencePort transactionRepository;

    @ConfigProperty(name = "scheduler.activation", defaultValue = "true")
    boolean activation;

    @ConfigProperty(name = "scheduler.every", defaultValue = "5s")
    String every;

    public PriorityQueue<Transaction> getTransactionQueue() {
        return transactionQueue;
    }

    @Override
    @Scheduled(every = "{scheduler.every}")
    public void processQueue() {
        if (isActivation()) {
            logger.info("Scheduler start every " + getEvery());
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

    public boolean isActivation() {
        return activation;
    }

    public String getEvery() {
        return every;
    }
}