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
        assertThat(dto.getAmount()).isEqualTo(0L);
        assertThat(dto.getType()).isNull();
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getDate()).isNull();
        assertThat(dto.getLabel()).isNull();
        assertThat(dto.getMetadatas()).isEmpty();
    }

    @Test
    public void toEntity_should_mapNullValues_when_dtoHasNullValues() {
        
        try {
            transactionMapper.toEntity(new Transaction());
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Bank Account entity not found for id: null");
            
        }

    }
    
    @Test
    public void toDto_should_mapEntityValues_when_entityHasValues() {
        Instant date = Instant.now();
        Map<String, String> metadatas = new HashMap<>();
        metadatas.put("amount_after", "100");
        metadatas.put("amount_before", "0");
        TransactionEntity transactionEntity = createTransactionEntity(10L, 99L, date);
        
        Transaction dto = transactionMapper.toDto(transactionEntity);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getAccountId()).isEqualTo(99L);
        assertThat(dto.getAmount()).isEqualTo(100L);
        assertThat(dto.getType()).isEqualTo(TransactionType.CREDIT);
        assertThat(dto.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(dto.getDate()).isEqualTo(date);
        assertThat(dto.getLabel()).hasToString("transaction test");
        assertThat(dto.getMetadatas()).usingRecursiveComparison().isEqualTo(metadatas);
    }
    
    @Test
    public void toEntity_should_mapEntityValues_when_dtoHasValues() {
        Instant date = Instant.now();
        String strMetadatas = "{\"amount_after\":\"100\",\"amount_before\":\"0\"}";
        Map<String, String> metadatas = new HashMap<>();
        metadatas.put("amount_after", "100");
        metadatas.put("amount_before", "0");
        BankAccountEntity bankAccountEntity = createBankAccountEntity(99L, date);
        Transaction transaction = createTransaction(10L, 99L, date);
        
        when(bankAccountRepository.findByIdOptional(99L)).thenReturn(Optional.of(bankAccountEntity));
        
        TransactionEntity entity = transactionMapper.toEntity(transaction);
        
        assertThat(entity.getId()).isEqualTo(10L);
        assertThat(entity.getAccount()).usingRecursiveComparison().isEqualTo(bankAccountEntity);
        assertThat(entity.getAmount()).usingRecursiveComparison().isEqualTo(new BigDecimal(100L));
        assertThat(entity.getType()).isEqualTo(TransactionType.CREDIT);
        assertThat(entity.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(entity.getDate()).isEqualTo(date);
        assertThat(entity.getLabel()).hasToString("transaction test");
        assertThat(entity.getMetadatas()).isEqualTo(strMetadatas);
    }
    
    private Transaction createTransaction(long id, long accountId, Instant instantDate) {
        Transaction transactionEntity = new Transaction();
        transactionEntity.setId(id);
        transactionEntity.setAccountId(accountId);
        transactionEntity.setAmount(100L);
        transactionEntity.setType(TransactionType.CREDIT);
        transactionEntity.setStatus(TransactionStatus.COMPLETED);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        Map<String, String> metadatas = new HashMap<>();
        metadatas.put("amount_after", "100");
        metadatas.put("amount_before", "0");
        transactionEntity.setMetadatas(metadatas);
        return transactionEntity;
    }

    private TransactionEntity createTransactionEntity(long id, long accountId, Instant instantDate) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(id);
        transactionEntity.setAccount(createBankAccountEntity(accountId, instantDate));
        transactionEntity.setAmount(new BigDecimal("100"));
        transactionEntity.setType(TransactionType.CREDIT);
        transactionEntity.setStatus(TransactionStatus.COMPLETED);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        transactionEntity.setMetadatas("{\"amount_after\" : \"100\", \"amount_before\" : \"0\"}");
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
