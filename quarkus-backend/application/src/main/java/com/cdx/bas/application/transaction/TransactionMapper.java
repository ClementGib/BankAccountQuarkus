package com.cdx.bas.application.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.application.bank.account.BankAccountRepository;
import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.MappingException;

@RequestScoped
public class TransactionMapper implements DtoEntityMapper<Transaction, TransactionEntity> {

    @Inject
    BankAccountRepository bankAccountRepository;

    @Inject
    ObjectMapper objectMapper;

    public Transaction toDto(TransactionEntity entity) {
        
        if (entity == null) {
            return null;
        }
        
        Transaction dto = new Transaction();

        dto.setId(entity.getId());
        
        if (entity.getAccount() != null) {
            dto.setAccountId(entity.getAccount().getId());
        }

        if (entity.getAmount() != null) {
            dto.setAmount(entity.getAmount());
        }
        dto.setCurrency(entity.getCurrency());
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        dto.setDate(entity.getDate());
        dto.setLabel(entity.getLabel());

        try {
            if (entity.getMetadata() != null) {
                dto.setMetadata(
                        objectMapper.readValue(entity.getMetadata(), new TypeReference<Map<String, String>>() {}));
            } else {
                dto.setMetadata(new HashMap<>());
            }
        } catch (JsonProcessingException exception) {
            throw new MappingException("An error occured while parsing JSON String to Map<String, String>", exception);
        }
        return dto;
    }

    public TransactionEntity toEntity(Transaction dto) {
        
        if (dto == null) {
            return null;
        }
        
        TransactionEntity entity = new TransactionEntity();
        entity.setId(dto.getId());
        Optional<BankAccountEntity> optionalBankAccountEntity = bankAccountRepository
                .findByIdOptional(dto.getAccountId());
        if (optionalBankAccountEntity.isPresent()) {
            entity.setAccount(optionalBankAccountEntity.get());
        } else {
            throw new NoSuchElementException("Bank Account entity not found for id: " + dto.getAccountId());
        }

        entity.setAmount(dto.getAmount());
        entity.setCurrency(dto.getCurrency());
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setDate(dto.getDate());
        entity.setLabel(dto.getLabel());

        try {
            if (!dto.getMetadata().isEmpty()) {
                entity.setMetadata(objectMapper.writeValueAsString(dto.getMetadata()));
            } else {
                entity.setMetadata(null);
            }
        } catch (JsonProcessingException exception) {
            throw new MappingException("An error occured while parsing Map<String, String> to JSON String", exception);
        }
        return entity;
    }
}
