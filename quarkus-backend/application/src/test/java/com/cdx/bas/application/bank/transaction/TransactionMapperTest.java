package com.cdx.bas.application.bank.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.cdx.bas.application.transaction.TransactionEntity;
import com.cdx.bas.application.transaction.TransactionMapper;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TransactionMapperTest {

    @Inject
    TransactionMapper transactionMapper;

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

        assertThat(dto.getId()).isZero();
        assertThat(dto.getAccountId()).usingRecursiveComparison().isEqualTo(new Money(null));
        assertThat(dto.getAmount()).isNull();
        assertThat(dto.getType()).isNull();
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getDate()).isNull();
        assertThat(dto.getLabel()).isNull();
        assertThat(dto.getMetadatas()).isNull();
    }

    @Test
    public void toEntity_should_mapNullValues_when_dtoHasNullValues() {
        TransactionEntity entity = transactionMapper.toEntity(new Transaction());

        assertThat(entity.getId()).isZero();
        assertThat(entity.getAccount()).isNull();
        assertThat(entity.getAmount()).isNull();
        assertThat(entity.getType()).isNull();
        assertThat(entity.getStatus()).isNull();
        assertThat(entity.getDate()).isNull();
        assertThat(entity.getLabel()).isNull();
        assertThat(entity.getMetadatas()).isNull();
    }

}
