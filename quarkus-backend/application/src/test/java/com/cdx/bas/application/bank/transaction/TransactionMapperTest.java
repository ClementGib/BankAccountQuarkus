package com.cdx.bas.application.bank.transaction;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.application.bank.account.BankAccountRepository;
import com.cdx.bas.application.transaction.TransactionEntity;
import com.cdx.bas.application.transaction.TransactionMapper;
import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.entry;


@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class TransactionMapperTest {

    @InjectMock
    private BankAccountRepository bankAccountRepository;

    @Inject
    TransactionMapper transactionMapper;

    @Test
    public void toDto_should_throwException_when_transactionDto_doesNotHave_senderBankAccount() {
        try {
            TransactionEntity transactionEntity = new TransactionEntity();
            BankAccountEntity bankAccountEntity = new BankAccountEntity();
            bankAccountEntity.setId(10L);
            transactionEntity.setSenderBankAccountEntity(bankAccountEntity);
            Transaction dto = transactionMapper.toDto(transactionEntity);
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Transaction does not have receiver bank account.");
        }
    }

    @Test
    public void toDto_should_throwException_when_transactionDto_doesNotHave_receiverBankAccount() {

        try {
            Transaction dto = transactionMapper.toDto(new TransactionEntity());
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Transaction does not have sender bank account.");
        }
    }

    @Test
    public void toEntity_should_throwException_when_transactionDto_doesNotHave_senderBankAccount() {

        try {
            Transaction transaction = new Transaction();
            transaction.setId(10L);
            transaction.setSenderAccountId(null);
            transactionMapper.toEntity(transaction);
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Transaction does not have sender bank account entity.");
        }
    }

    @Test
    public void toEntity_should_throwException_when_transactionDto_doesNotHave_receiverBankAccount() {

        try {
            Transaction transaction = new Transaction();
            transaction.setId(10L);
            transaction.setSenderAccountId(99L);
            transaction.setReceiverAccountId(null);

            when(bankAccountRepository.findByIdOptional(99L)).thenReturn(Optional.of(new BankAccountEntity()));

            transactionMapper.toEntity(transaction);
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Transaction does not have receiver bank account entity.");
        }
    }
    
    @Test
    public void toDto_should_mapEntityValues_when_entityHasValues() {
        Instant date = Instant.now();
        long senderAccountId = 99L;
        long receiverAccountId = 77L;
        Map<String, String> metadata = Map.of("amount_before", "0", "amount_after", "100");
        TransactionEntity transactionEntity = createTransactionEntity(10L, senderAccountId, receiverAccountId, date);

        Transaction dto = transactionMapper.toDto(transactionEntity);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getSenderAccountId()).isEqualTo(senderAccountId);
        assertThat(dto.getReceiverAccountId()).isEqualTo(receiverAccountId);
        assertThat(dto.getAmount()).isEqualTo(new BigDecimal("100"));
        assertThat(dto.getCurrency()).isEqualTo("EUR");
        assertThat(dto.getType()).isEqualTo(TransactionType.CREDIT);
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(dto.getDate()).isEqualTo(date);
        assertThat(dto.getLabel()).hasToString("transaction test");
        assertThat(dto.getMetadata()).usingRecursiveComparison().isEqualTo(metadata);
    }
    
    @Test
    public void toEntity_should_mapEntityValues_when_dtoHasValues() {
        Instant date = Instant.now();
        long senderAccountId = 99L;
        long receiverAccountId = 77L;
        String strMetadata = "{\"amount_after\":\"100\",\"amount_before\":\"0\"}";
        Map<String, String> metadata = new HashMap<>();
        metadata.put("amount_before", "0");
        metadata.put("amount_after", "100");
        BankAccountEntity senderBankAccountEntity = createBankAccountEntity(senderAccountId);
        BankAccountEntity receiverBankAccountEntity = createBankAccountEntity(receiverAccountId);
        Transaction transaction = createTransaction(10L, senderAccountId, receiverAccountId, date);
        transaction.setMetadata(metadata);

        when(bankAccountRepository.findByIdOptional(99L)).thenReturn(Optional.of(senderBankAccountEntity));
        when(bankAccountRepository.findByIdOptional(77L)).thenReturn(Optional.of(receiverBankAccountEntity));
        
        TransactionEntity entity = transactionMapper.toEntity(transaction);
        
        assertThat(entity.getId()).isEqualTo(10L);
        assertThat(entity.getSenderBankAccountEntity()).usingRecursiveComparison().isEqualTo(senderBankAccountEntity);
        assertThat(entity.getReceiverBankAccountEntity()).usingRecursiveComparison().isEqualTo(receiverBankAccountEntity);
        assertThat(entity.getAmount()).usingRecursiveComparison().isEqualTo(new BigDecimal(100));
        assertThat(entity.getCurrency()).isEqualTo("EUR");
        assertThat(entity.getType()).isEqualTo(TransactionType.CREDIT);
        assertThat(entity.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(entity.getDate()).isEqualTo(date);
        assertThat(entity.getLabel()).hasToString("transaction test");
        assertThat(entity.getMetadata()).contains(strMetadata);
    }
    
    private Transaction createTransaction(long id, long senderAccountId, long receiverAccountId, Instant instantDate) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setSenderAccountId(senderAccountId);
        transaction.setReceiverAccountId(receiverAccountId);
        transaction.setAmount(new BigDecimal(100));
        transaction.setCurrency("EUR");
        transaction.setType(TransactionType.CREDIT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setDate(instantDate);
        transaction.setLabel("transaction test");
        Map<String, String> metadata = Map.of("amount_before", "0", "amount_after", "100");
        transaction.setMetadata(metadata);
        return transaction;
    }

    private TransactionEntity createTransactionEntity(long id, long senderAccountId, long receiverAccountId, Instant instantDate) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(id);
        transactionEntity.setSenderBankAccountEntity(createBankAccountEntity(senderAccountId));
        transactionEntity.setReceiverBankAccountEntity(createBankAccountEntity(receiverAccountId));
        transactionEntity.setAmount(new BigDecimal("100"));
        transactionEntity.setCurrency("EUR");
        transactionEntity.setType(TransactionType.CREDIT);
        transactionEntity.setStatus(TransactionStatus.COMPLETED);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        transactionEntity.setMetadata("{\"amount_after\" : \"100\", \"amount_before\" : \"0\"}");
        return transactionEntity;
    }
    
    private BankAccountEntity createBankAccountEntity(long id) {
        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setId(id);
        bankAccountEntity.setType(AccountType.CHECKING);
        bankAccountEntity.setBalance(new BigDecimal("100"));
        HashSet<Long> customersId = new HashSet<>();
        customersId.add(99L);
        return bankAccountEntity;
    }
}