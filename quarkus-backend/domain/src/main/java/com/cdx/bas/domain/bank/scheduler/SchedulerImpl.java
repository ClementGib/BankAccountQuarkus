package com.cdx.bas.domain.bank.scheduler;

import java.time.Instant;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.cdx.bas.domain.bank.account.BankAccountService;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionService;

import org.jboss.logging.Logger;

import io.quarkus.runtime.Startup;

@Startup
@Singleton
@ApplicationScoped
@Transactional(value = TxType.NEVER)
public class SchedulerImpl implements Scheduler {
    
    private static final Logger logger = Logger.getLogger(SchedulerImpl.class);
    
    @Inject
    TransactionService transactionService;
    
    @Inject
    BankAccountService bankAccountService;

    private static PriorityQueue<Transaction> queue = new PriorityQueue<Transaction>(10, new Comparator<Transaction>() {
        @Override
        public int compare(Transaction firstTransaction, Transaction secondTransaction)
        {
            Instant firstDate = firstTransaction.date();
            Instant secondDate = secondTransaction.date();
            return firstDate.compareTo(secondDate);
        }
    });
    
    @Override
    public void processQueue() {
        // log starting
        // get queue from service
        // treat them ->
            //log
            // call account service to treat each
        // log end
        
    }
}
