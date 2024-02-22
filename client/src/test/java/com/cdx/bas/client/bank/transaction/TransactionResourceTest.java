package com.cdx.bas.client.bank.transaction;

import com.cdx.bas.domain.bank.transaction.Transaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.cdx.bas.domain.bank.transaction.type.TransactionType.CREDIT;
import static com.cdx.bas.domain.bank.transaction.type.TransactionType.DEBIT;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class TransactionResourceTest {

    @Inject
    TransactionResource transactionResource;

    @Test
    public void getAll_shouldReturnAllTransactions() {
        Set<Transaction> expectedCustomers = Set.of(
                new Transaction(1L, 1L, 2L, new BigDecimal("1600.00"), "EUR", CREDIT, COMPLETED, Instant.parse("2024-06-06T11:00:00+00:00"), "transaction 1", Map.of("sender_amount_before", "2000", "receiver_amount_before", "0", "sender_amount_after", "400", "receiver_amount_after", "1600")),
                new Transaction(2L, 6L, 3L, new BigDecimal("9200.00"), "EUR", CREDIT, ERROR, Instant.parse("2024-07-10T14:00:00+00:00"), "transaction 2", Map.of("error", "Transaction 2 deposit error for amount 9200 ...")),
                new Transaction(3L, 6L, 3L, new BigDecimal("9200.00"), "EUR", CREDIT, COMPLETED, Instant.parse("2024-07-10T14:00:00+00:00"), "transaction 3", Map.of("sender_amount_before", "9200", "receiver_amount_before", "10000", "sender_amount_after", "0", "receiver_amount_after", "19200")),
                new Transaction(4L, 5L, 1L, new BigDecimal("100000.00"), "EUR", CREDIT, REFUSED, Instant.parse("2024-07-10T14:00:00+00:00"), "transaction 4", Map.of("error", "Transaction 4 deposit error for amount 100000 ...")),
                new Transaction(5L, 2L, 1L, new BigDecimal("600.99"), "EUR", CREDIT, UNPROCESSED, Instant.parse("2024-11-06T17:00:00+00:00"), "transaction 5", new HashMap<>()),
                new Transaction(6L, 1L, 7L, new BigDecimal("2000.00"), "EUR", DEBIT, UNPROCESSED, Instant.parse("2024-11-06T17:30:00+00:00"), "transaction 6", new HashMap<>()),
                new Transaction(7L, 3L, 1L, new BigDecimal("1000.00"), "EUR", CREDIT, UNPROCESSED, Instant.parse("2024-12-06T17:00:00+00:00"), "transaction 7", new HashMap<>()),
                new Transaction(8L, 4L, 2L, new BigDecimal("300.80"), "EUR", DEBIT, UNPROCESSED, Instant.parse("2024-12-06T18:00:00+00:00"), "transaction 8", new HashMap<>()),
                new Transaction(9L, 8L, 7L, new BigDecimal("5000.00"), "EUR", DEBIT, UNPROCESSED, Instant.parse("2024-12-06T18:00:10+00:00"), "transaction 9", new HashMap<>())
        );

        Set<Transaction> actualTransactions = transactionResource.getAll();
        assertThat(actualTransactions)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedCustomers);
    }

    @Test
    public void getAllByStatus_shouldReturnEmptySet_whenStatusIsInvalid() {
        Set<Transaction> expectedCustomers = Collections.emptySet();

        Set<Transaction> actualTransactions = transactionResource.getAllByStatus("");
        assertThat(actualTransactions)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedCustomers);
    }

    @Test
    public void getAllByStatus_shouldReturnTransactionWithCompletedStatus() {
        Set<Transaction> expectedCustomers = Set.of(
                new Transaction(1L, 1L, 2L, new BigDecimal("1600.00"), "EUR", CREDIT, COMPLETED, Instant.parse("2024-06-06T11:00:00+00:00"), "transaction 1", Map.of("sender_amount_before", "2000", "receiver_amount_before", "0", "sender_amount_after", "400", "receiver_amount_after", "1600")),
                new Transaction(3L, 6L, 3L, new BigDecimal("9200.00"), "EUR", CREDIT, COMPLETED, Instant.parse("2024-07-10T14:00:00+00:00"), "transaction 3", Map.of("sender_amount_before", "9200", "receiver_amount_before", "10000", "sender_amount_after", "0", "receiver_amount_after", "19200"))
        );

        Set<Transaction> actualTransactions = transactionResource.getAllByStatus("completed");
        assertThat(actualTransactions)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedCustomers);
    }

    @Test
    public void getAllByStatus_shouldReturnTransactionWithUnprocessedStatus() {
        Set<Transaction> expectedCustomers = Set.of(
                new Transaction(5L, 2L, 1L, new BigDecimal("600.99"), "EUR", CREDIT, UNPROCESSED, Instant.parse("2024-11-06T17:00:00+00:00"), "transaction 5", new HashMap<>()),
                new Transaction(6L, 1L, 7L, new BigDecimal("2000.00"), "EUR", DEBIT, UNPROCESSED, Instant.parse("2024-11-06T17:30:00+00:00"), "transaction 6", new HashMap<>()),
                new Transaction(7L, 3L, 1L, new BigDecimal("1000.00"), "EUR", CREDIT, UNPROCESSED, Instant.parse("2024-12-06T17:00:00+00:00"), "transaction 7", new HashMap<>()),
                new Transaction(8L, 4L, 2L, new BigDecimal("300.80"), "EUR", DEBIT, UNPROCESSED, Instant.parse("2024-12-06T18:00:00+00:00"), "transaction 8", new HashMap<>()),
                new Transaction(9L, 8L, 7L, new BigDecimal("5000.00"), "EUR", DEBIT, UNPROCESSED, Instant.parse("2024-12-06T18:00:10+00:00"), "transaction 9", new HashMap<>())
        );

        Set<Transaction> actualTransactions = transactionResource.getAllByStatus("unprocessed");
        assertThat(actualTransactions)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedCustomers);
    }

    @Test
    public void getAllByStatus_shouldReturnTransactionWithErrorStatus() {
        Set<Transaction> expectedCustomers = Set.of(
                new Transaction(2L, 6L, 3L, new BigDecimal("9200.00"), "EUR", CREDIT, ERROR, Instant.parse("2024-07-10T14:00:00+00:00"), "transaction 2", Map.of("error", "Transaction 2 deposit error for amount 9200 ..."))
        );

        Set<Transaction> actualTransactions = transactionResource.getAllByStatus("error");
        assertThat(actualTransactions)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedCustomers);
    }

    @Test
    public void getAllByStatus_shouldReturnTransactionWithRefusedStatus() {
        Set<Transaction> expectedCustomers = Set.of(
                new Transaction(4L, 5L, 1L, new BigDecimal("100000.00"), "EUR", CREDIT, REFUSED, Instant.parse("2024-07-10T14:00:00+00:00"), "transaction 4", Map.of("error", "Transaction 4 deposit error for amount 100000 ..."))
        );

        Set<Transaction> actualTransactions = transactionResource.getAllByStatus("refused");
        assertThat(actualTransactions)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedCustomers);
    }

    @Test
    public void deposit_shouldReturnAccepted202Response_whenTransactionIsValidated() {
        Transaction validTransaction =  new Transaction(null, 1L, 2L, new BigDecimal("1000.00"), "EUR", CREDIT, UNPROCESSED, Instant.parse("2024-06-06T11:00:00+00:00"), "transaction 1", new HashMap<>());

       Response actualResponse = transactionResource.deposit(1L, validTransaction);
       assertThat(actualResponse).isEqualTo("");
    }

    @Test
    public void deposit_shouldReturnError400Response_whenTransactionIsInvalid() {
        Transaction validTransaction =  new Transaction(null, 1L, 2L, new BigDecimal("1000.00"), "EUR", CREDIT, UNPROCESSED, Instant.parse("2024-06-06T11:00:00+00:00"), "transaction 1", new HashMap<>());

        Response actualResponse = transactionResource.deposit(1L, validTransaction);
        assertThat(actualResponse).isEqualTo("");
    }

    @Test
    public void deposit_shouldReturnError500Response_whenInternalErrorHappened() {
        Transaction validTransaction =  new Transaction(null, 1L, 2L, new BigDecimal("1000.00"), "EUR", CREDIT, UNPROCESSED, Instant.parse("2024-06-06T11:00:00+00:00"), "transaction 1", new HashMap<>());

        Response actualResponse = transactionResource.deposit(1L, validTransaction);
        assertThat(actualResponse).isEqualTo("");
    }
}