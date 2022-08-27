package com.cdx.bas.domain.bank.scheduler;

import java.util.Queue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.cdx.bas.domain.bank.transaction.Transaction;

import io.quarkus.runtime.Startup;

@Startup
@Singleton
@ApplicationScoped
@Transactional(value = TxType.NEVER)
public class SchedulerImpl implements Scheduler {
    
    @Inject

    private static Queue<Transaction> queue;

    @Override
    public void processQueue() {
        // TODO Auto-generated method stub
        
    }
}
