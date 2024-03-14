package com.cdx.bas.application.bank.transaction;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.application.bank.account.BankAccountRepository;
import com.cdx.bas.application.bank.account.BankAccountEntityUtils;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.type.TransactionType;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class TransactionMapperTest {

    @InjectMock
    BankAccountRepository bankAccountRepository;

    @Inject
    TransactionMapper transactionMapper;

    @Test
    public void toDto_shouldThrowException_whenTransactionDtoDoesNotHaveReceiverBankAccount() {
        try {
            BankAccountEntity bankAccountEntity = new BankAccountEntity();
            TransactionEntity transactionEntity = new TransactionEntity();
            transactionEntity.setEmitterBankAccountEntity(bankAccountEntity);
            bankAccountEntity.setId(10L);
            transactionMapper.toDto(transactionEntity);
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Transaction does not have receiver bank account.");
        }
    }

    @Test
    public void toDto_shouldThrowException_whenTransactionDtoDoesNotHaveEmitterBankAccount() {
        try {
            transactionMapper.toDto(new TransactionEntity());
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Transaction does not have emitter bank account.");
        }
    }

    @Test
    public void toEntity_shouldThrowException_whenTransactionDtoDoesNotHaveEmitterBankAccount() {

        try {
            Transaction transaction = Transaction.builder()
                    .id(10L)
                    .emitterAccountId(null)
                    .build();
            transactionMapper.toEntity(transaction);
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Transaction does not have emitter bank account entity.");
        }
    }

    @Test
    public void toEntity_shouldThrowException_whenTransactionDto_doesNotHave_receiverBankAccount() {

        try {
            Transaction transaction = Transaction.builder()
                    .id(10L)
                    .emitterAccountId(99L)
                    .receiverAccountId(null)
                    .build();

            when(bankAccountRepository.findByIdOptional(99L)).thenReturn(Optional.of(new BankAccountEntity()));

            transactionMapper.toEntity(transaction);
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Transaction does not have receiver bank account entity.");
        }
    }
    
    @Test
    public void toDto_shouldMapEntityValues_whenEntityHasValues() {
        Instant date = Instant.now();
        long emitterAccountId = 99L;
        long receiverAccountId = 77L;
        Map<String, String> metadata = Map.of("amount_before", "0", "amount_after", "100");
        TransactionEntity transactionEntity = TransactionTestUtils.createTransactionEntityUtils(emitterAccountId, receiverAccountId, date);

        Transaction dto = transactionMapper.toDto(transactionEntity);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getEmitterAccountId()).isEqualTo(emitterAccountId);
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
    public void toEntity_shouldMapEntityValues_whenDtoHasValues() {
        Instant date = Instant.now();
        long emitterAccountId = 99L;
        long receiverAccountId = 77L;
        String strMetadata = "{\"amount_after\":\"100\",\"amount_before\":\"0\"}";
        Map<String, String> metadata = new HashMap<>();
        metadata.put("amount_before", "0");
        metadata.put("amount_after", "100");
        BankAccountEntity emitterBankAccountEntity = BankAccountEntityUtils.createBankAccountEntity(emitterAccountId);
        BankAccountEntity receiverBankAccountEntity = BankAccountEntityUtils.createBankAccountEntity(receiverAccountId);
        Transaction transaction = TransactionTestUtils.createTransactionUtils(emitterAccountId, receiverAccountId, date);
        transaction.setMetadata(metadata);

        when(bankAccountRepository.findByIdOptional(99L)).thenReturn(Optional.of(emitterBankAccountEntity));
        when(bankAccountRepository.findByIdOptional(77L)).thenReturn(Optional.of(receiverBankAccountEntity));
        
        TransactionEntity entity = transactionMapper.toEntity(transaction);
        
        assertThat(entity.getId()).isEqualTo(10L);
        assertThat(entity.getEmitterBankAccountEntity()).usingRecursiveComparison().isEqualTo(emitterBankAccountEntity);
        assertThat(entity.getReceiverBankAccountEntity()).usingRecursiveComparison().isEqualTo(receiverBankAccountEntity);
        assertThat(entity.getAmount()).usingRecursiveComparison().isEqualTo(new BigDecimal(100));
        assertThat(entity.getCurrency()).isEqualTo("EUR");
        assertThat(entity.getType()).isEqualTo(TransactionType.CREDIT);
        assertThat(entity.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(entity.getDate()).isEqualTo(date);
        assertThat(entity.getLabel()).hasToString("transaction test");
        assertThat(entity.getMetadata()).contains(strMetadata);
    }
}