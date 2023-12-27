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
    TransactionRepository transactionRepository;

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
        
        if (entity.getSenderAccount() != null) {
            dto.setSenderAccountId(entity.getSenderAccount().getId());
        }

        if (entity.getReceiverAccount() != null) {
            dto.setReceiverAccountId(entity.getReceiverAccount().getId());
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
            throw new MappingException("Mapping error: An error occurred while parsing JSON String to Map<String, String>", exception);
        }
        return dto;
    }

    public TransactionEntity toEntity(Transaction dto) {
        
        if (dto == null || dto.getId() == null) {
            return null;
        }
        
        TransactionEntity entity = transactionRepository.findByIdOptional(dto.getId()).orElse(new TransactionEntity());
        entity.setId(dto.getId());

        Optional<BankAccountEntity> optionalSenderBankAccountEntity = bankAccountRepository.findByIdOptional(dto.getSenderAccountId());
        if (optionalSenderBankAccountEntity.isPresent()) {
            entity.setSenderAccount(optionalSenderBankAccountEntity.get());
        } else {
            throw new NoSuchElementException("Mapping error: sender bank account entity is not found for id: " + dto.getSenderAccountId());
        }

        Optional<BankAccountEntity> optionalReceiverBankAccountEntity = bankAccountRepository.findByIdOptional(dto.getReceiverAccountId());
        if (optionalReceiverBankAccountEntity.isPresent()) {
            entity.setReceiverAccount(optionalReceiverBankAccountEntity.get());
        } else {
            throw new NoSuchElementException("Mapping error: receiver bank account entity is not found for id: " + dto.getReceiverAccountId());
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
            throw new MappingException("Mapping error: An error occurred while parsing Map<String, String> to JSON String", exception);
        }
        return entity;
    }
}
