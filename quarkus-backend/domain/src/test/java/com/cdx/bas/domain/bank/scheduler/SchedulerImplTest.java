package com.cdx.bas.domain.bank.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.PriorityQueue;

import javax.inject.Inject;

import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionService;
import com.cdx.bas.domain.bank.transaction.TransactionType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class SchedulerImplTest {
    
    @Inject
    Scheduler scheduler;
    
    @InjectMock
    TransactionService transactionService;
    
    @Test
    @Order(1)
    public void processQueue_should_fillTheQueueAndProcessOrderedTransactions_when_QueueWasEmptyAndWillBeFilledWithOrderedTransactions() {
        PriorityQueue<Transaction> queue = createDepositTransactions();
        when(transactionService.getUnprocessedTransactions()).thenReturn(queue);
        Clock clock;
        
        scheduler.processQueue();
        
        verify(transactionService).getUnprocessedTransactions();
        assertThat(queue.peek()).usingRecursiveComparison()
        .isEqualTo(new Transaction(16L, 20L, TransactionType.CREDIT, Instant.MIN, "Fifth transaction"));
        verify(transactionService).processTransaction(queue.poll());
        clock = Clock.fixed(Instant.parse("2022-12-06T10:14:00Z"), ZoneId.of("UTC"));
        assertThat(queue.peek()).usingRecursiveComparison()
        .isEqualTo(new Transaction(50L, 120L, TransactionType.CREDIT, Instant.now(clock), "Third transaction"));
        verify(transactionService).processTransaction(queue.poll());
        clock = Clock.fixed(Instant.parse("2022-12-07T10:14:00Z"), ZoneId.of("UTC"));
        assertThat(queue.peek()).usingRecursiveComparison()
        .isEqualTo(new Transaction(100L, 99L, TransactionType.CREDIT, Instant.now(clock), "Second transaction"));
        verify(transactionService).processTransaction(queue.poll());
        clock = Clock.fixed(Instant.parse("2022-12-07T10:18:00Z"), ZoneId.of("UTC"));
        assertThat(queue.peek()).usingRecursiveComparison()
        .isEqualTo(new Transaction(99L, 1000L, TransactionType.CREDIT, Instant.now(clock), "Fourth transaction"));
        verify(transactionService).processTransaction(queue.poll());
        
        assertThat(queue.peek()).usingRecursiveComparison()
        .isEqualTo(new Transaction(99L, 250L, TransactionType.CREDIT, Instant.MAX, "First transaction"));
        verify(transactionService).processTransaction(queue.poll());
        verifyNoMoreInteractions(transactionService);
    }
    
    @Test
    @Order(2)
    public void processQueue_should_DoNothing_when_SchedulerIsAlreadyInUse() throws InterruptedException {
        scheduler.processQueue();
        verifyNoInteractions(transactionService);
    }
    
    @Test
    @Order(3)
    public void processQueue_should_runScheduleProcessQueues_when_WaitingForScheduler() throws InterruptedException {
        PriorityQueue<Transaction> queue = createDepositTransactions();
        when(transactionService.getUnprocessedTransactions()).thenReturn(queue);
        
        Thread.sleep(5000);
        
        verify(transactionService).getUnprocessedTransactions();
        verify(transactionService, times(5)).processTransaction(any());
        verifyNoMoreInteractions(transactionService);
    }
    
    PriorityQueue<Transaction> createDepositTransactions() {
        Clock clock;      
        PriorityQueue<Transaction> queue = new PriorityQueue<Transaction>();
        queue.add(new Transaction(99L, 250L, TransactionType.CREDIT, Instant.MAX, "First transaction"));
        clock = Clock.fixed(Instant.parse("2022-12-07T10:14:00Z"), ZoneId.of("UTC"));
        queue.add(new Transaction(100L, 99L, TransactionType.CREDIT, Instant.now(clock), "Second transaction"));
        clock = Clock.fixed(Instant.parse("2022-12-06T10:14:00Z"), ZoneId.of("UTC"));
        queue.add(new Transaction(50L, 120L, TransactionType.CREDIT, Instant.now(clock), "Third transaction"));
        clock = Clock.fixed(Instant.parse("2022-12-07T10:18:00Z"), ZoneId.of("UTC"));
        queue.add(new Transaction(99L, 1000L, TransactionType.CREDIT, Instant.now(clock), "Fourth transaction"));
        queue.add(new Transaction(16L, 20L, TransactionType.CREDIT, Instant.MIN, "Fifth transaction"));
        return queue;
    }
}
