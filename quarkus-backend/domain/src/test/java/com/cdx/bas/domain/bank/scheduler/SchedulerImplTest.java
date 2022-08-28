package com.cdx.bas.domain.bank.scheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountService;
import com.cdx.bas.domain.bank.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionService;
import com.cdx.bas.domain.bank.transaction.TransactionType;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SchedulerImplTest {
    
    
    @InjectMocks
    Scheduler scheduler;
    
    @Mock
    TransactionService transactionService;
    
    @Mock
    BankAccountService bankAccountService;

    @Test
    public void processQueue_should_processAllTheTransactionInTheQueue_when_QueueIsFullOfTransactions() {
        PriorityQueue<Transaction> queue = createDepositTransactions();
        when(transactionService.getUnprocessedTransactions()).thenReturn(queue);
        when(bankAccountService.deposit(any(), anyLong())).thenReturn(true);
        
        scheduler.processQueue();
        
        verify(transactionService).getUnprocessedTransactions();
        verify(bankAccountService).deposit(Money.of(99L), 250L);
        verify(bankAccountService).deposit(Money.of(100L), 99L);
        verify(bankAccountService).deposit(Money.of(50L), 120L);
        verify(bankAccountService).deposit(Money.of(99L), 1000L);
        verify(bankAccountService).deposit(Money.of(16L), 20L);
        verifyNoMoreInteractions(transactionService, bankAccountService);
    }
    
    PriorityQueue<Transaction> createDepositTransactions() {
        PriorityQueue<Transaction> queue = new PriorityQueue<Transaction>();
        queue.add(new Transaction(99L, 250L, TransactionType.CREDIT, Instant.MAX, "First transaction"));
        queue.add(new Transaction(100L, 99L, TransactionType.CREDIT, Instant.now(), "Second transaction"));
        queue.add(new Transaction(50L, 120L, TransactionType.CREDIT, Instant.now(), "Third transaction"));
        queue.add(new Transaction(99L, 1000L, TransactionType.CREDIT, Instant.now(), "Fourth transaction"));
        queue.add(new Transaction(16L, 20L, TransactionType.CREDIT, Instant.MIN, "Fifth transaction"));
        return queue;
    }
}
