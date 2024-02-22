package com.cdx.bas.application.scheduler;

import com.cdx.bas.application.bank.transaction.TransactionTestUtils;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionPersistencePort;
import com.cdx.bas.domain.bank.transaction.TransactionServicePort;
import com.cdx.bas.domain.transaction.*;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@QuarkusTest
@TestProfile(SchedulerTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class SchedulerTest {

    @InjectMock
    TransactionServicePort transactionService;

    @InjectMock
    TransactionPersistencePort transactionRepository;

    @Inject
    Scheduler scheduler;

    @Order(1)
    @Test
    public void processQueue_shouldFillTheQueue_whenQueueWasEmpty() {
        when(transactionRepository.findUnprocessedTransactions()).thenReturn(new PriorityQueue<>());

        scheduler.processQueue();

        verify(transactionRepository).findUnprocessedTransactions();
        verifyNoMoreInteractions(transactionRepository);
        verifyNoInteractions(transactionService);
    }
    
    @Order(2)
    @Test
    public void processQueue_shouldRunSchedulerProcess_withOrderedQueues_whenQueueIsFilled_withUnprocessedTransactions() {
        Queue<Transaction> queue = createCreditTransactionsUtils();
        when(transactionRepository.findUnprocessedTransactions()).thenReturn(queue);
        Clock clock;

        scheduler.processQueue();

        assertThat(queue.peek()).usingRecursiveComparison().isEqualTo (TransactionTestUtils.createTransactionUtils(5L, 59L, Instant.MIN, "Fifth transaction"));
        verify(transactionService).process(queue.poll());
        clock = Clock.fixed(Instant.parse("2022-12-06T10:14:00Z"), ZoneId.of("UTC"));
        assertThat(queue.peek()).usingRecursiveComparison().isEqualTo (TransactionTestUtils.createTransactionUtils(3L, 150, Instant.now(clock), "Third transaction"));
        verify(transactionService).process(queue.poll());
        clock = Clock.fixed(Instant.parse("2022-12-07T10:14:00Z"), ZoneId.of("UTC"));
        assertThat(queue.peek()).usingRecursiveComparison().isEqualTo (TransactionTestUtils.createTransactionUtils(2L, 399L, Instant.now(clock), "Second transaction"));
        verify(transactionService).process(queue.poll());
        clock = Clock.fixed(Instant.parse("2022-12-07T10:18:00Z"), ZoneId.of("UTC"));
        assertThat(queue.peek()).usingRecursiveComparison().isEqualTo (TransactionTestUtils.createTransactionUtils(4L, 1000L, Instant.now(clock), "Fourth transaction"));
        verify(transactionService).process(queue.poll());

        verify(transactionRepository).findUnprocessedTransactions();
        verify(transactionService, times(5)).process(any());
        verifyNoMoreInteractions(transactionRepository, transactionService);
    }

    static Queue<Transaction> createCreditTransactionsUtils() {
        Clock clock;
        Queue<Transaction> queue = new PriorityQueue<>();
        queue.add (TransactionTestUtils.createTransactionUtils(1L, 250L, Instant.MAX, "First transaction"));
        clock = Clock.fixed(Instant.parse("2022-12-07T10:14:00Z"), ZoneId.of("UTC"));
        queue.add (TransactionTestUtils.createTransactionUtils(2L, 399L, Instant.now(clock), "Second transaction"));
        clock = Clock.fixed(Instant.parse("2022-12-06T10:14:00Z"), ZoneId.of("UTC"));
        queue.add (TransactionTestUtils.createTransactionUtils(3L, 150, Instant.now(clock), "Third transaction"));
        clock = Clock.fixed(Instant.parse("2022-12-07T10:18:00Z"), ZoneId.of("UTC"));
        queue.add (TransactionTestUtils.createTransactionUtils(4L, 1000L, Instant.now(clock), "Fourth transaction"));
        queue.add (TransactionTestUtils.createTransactionUtils(5L, 59L, Instant.MIN, "Fifth transaction"));
        return queue;
    }
}