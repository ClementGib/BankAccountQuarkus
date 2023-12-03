package com.cdx.bas.application.bank.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.application.bank.account.BankAccountRepository;
import com.cdx.bas.application.transaction.TransactionEntity;
import com.cdx.bas.application.transaction.TransactionMapper;
import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TransactionMapperTest {

    @Inject
    TransactionMapper transactionMapper;
    
    @InjectMock
    private BankAccountRepository bankAccountRepository;

    @Test
    public void toDto_should_returnNullDto_when_entityIsNull() {
        TransactionEntity entity = null;

        Transaction dto = transactionMapper.toDto(entity);

        assertThat(dto).isNull();
    }

    @Test
    public void toEntity_should_returnNullEntity_when_dtoIsNull() {
        Transaction dto = null;

        TransactionEntity entity = transactionMapper.toEntity(dto);

        assertThat(entity).isNull();
    }

    @Test
    public void toDto_should_mapNullValues_when_entityHasNullValues() {
        Transaction dto = transactionMapper.toDto(new TransactionEntity());

        assertThat(dto.getId()).isNull();
        assertThat(dto.getAccountId()).isNull();
        assertThat(dto.getAmount()).isNull();
        assertThat(dto.getCurrency()).isNull();
        assertThat(dto.getType()).isNull();
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getDate()).isNull();
        assertThat(dto.getLabel()).isNull();
        assertThat(dto.getMetadata()).isEmpty();
    }

    @Test
    public void toEntity_should_mapNullValues_when_dtoHasIdThatDoestMatchWithBankAccount() {
        
        try {
            Transaction transaction = new Transaction();
            transaction.setId(666L);
            transactionMapper.toEntity(transaction);
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Bank Account entity not found for id: null");
            
        }

    }
    
    @Test
    public void toDto_should_mapEntityValues_when_entityHasValues() {
        Instant date = Instant.now();
        Map<String, String> metadata = Map.of("amount_before", "0", "amount_after", "100");
        TransactionEntity transactionEntity = createTransactionEntity(10L, 99L, date);
        
        Transaction dto = transactionMapper.toDto(transactionEntity);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getAccountId()).isEqualTo(99L);
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
        String strMetadata = "{\"amount_after\":\"100\",\"amount_before\":\"0\"}";
        Map<String, String> metadata = Map.of("amount_before", "0", "amount_after", "100");
        BankAccountEntity bankAccountEntity = createBankAccountEntity(99L, date);
        Transaction transaction = createTransaction(10L, 99L, date);
        
        when(bankAccountRepository.findByIdOptional(99L)).thenReturn(Optional.of(bankAccountEntity));
        
        TransactionEntity entity = transactionMapper.toEntity(transaction);
        
        assertThat(entity.getId()).isEqualTo(10L);
        assertThat(entity.getAccount()).usingRecursiveComparison().isEqualTo(bankAccountEntity);
        assertThat(entity.getAmount()).usingRecursiveComparison().isEqualTo(new BigDecimal(100));
        assertThat(entity.getCurrency()).isEqualTo("EUR");
        assertThat(entity.getType()).isEqualTo(TransactionType.CREDIT);
        assertThat(entity.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(entity.getDate()).isEqualTo(date);
        assertThat(entity.getLabel()).hasToString("transaction test");
        assertThat(entity.getMetadata()).isEqualTo(strMetadata);
    }
    
    private Transaction createTransaction(long id, long accountId, Instant instantDate) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAccountId(accountId);
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

    private TransactionEntity createTransactionEntity(long id, long accountId, Instant instantDate) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(id);
        transactionEntity.setAccount(createBankAccountEntity(accountId, instantDate));
        transactionEntity.setAmount(new BigDecimal("100"));
        transactionEntity.setCurrency("EUR");
        transactionEntity.setType(TransactionType.CREDIT);
        transactionEntity.setStatus(TransactionStatus.COMPLETED);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        transactionEntity.setMetadata("{\"amount_after\" : \"100\", \"amount_before\" : \"0\"}");
        return transactionEntity;
    }
    
    private BankAccountEntity createBankAccountEntity(long id, Instant instantDate) {
        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setId(id);
        bankAccountEntity.setType(AccountType.CHECKING);
        bankAccountEntity.setBalance(new BigDecimal("100"));
        HashSet<Long> customersId = new HashSet<>();
        customersId.add(99L);
        return bankAccountEntity;
    }
}