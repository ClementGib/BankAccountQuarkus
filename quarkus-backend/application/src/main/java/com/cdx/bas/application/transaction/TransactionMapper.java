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

import javax.persistence.Entity;

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
        
        if (entity.getTransactionBankAccounts() != null && entity.getTransactionBankAccounts().getSenderAccount() != null) {
            dto.setSenderAccountId(entity.getTransactionBankAccounts().getSenderAccount().getId());
        }

        if (entity.getTransactionBankAccounts() != null && entity.getTransactionBankAccounts().getReceiverAccount() != null) {
            dto.setReceiverAccountId(entity.getTransactionBankAccounts().getReceiverAccount().getId());
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

        TransactionBankAccountsEntity transactionBankAccountsEntity = getTransactionBankAccountsEntity(dto);
        entity.setTransactionBankAccounts(transactionBankAccountsEntity);
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

    private TransactionBankAccountsEntity getTransactionBankAccountsEntity(Transaction dto) {
        Optional<BankAccountEntity> optionalSenderBankAccountEntity = bankAccountRepository.findByIdOptional(dto.getSenderAccountId());
        Optional<BankAccountEntity> optionalReceiverBankAccountEntity = bankAccountRepository.findByIdOptional(dto.getReceiverAccountId());
        TransactionBankAccountsEntity transactionBankAccountsEntity = new TransactionBankAccountsEntity();

        if (optionalSenderBankAccountEntity.isPresent()) {
            transactionBankAccountsEntity.setSenderAccount(optionalSenderBankAccountEntity.get());
        } else {
            throw new NoSuchElementException("Mapping error: sender bank account entity is not found for id: " + dto.getSenderAccountId());
        }

        if (optionalReceiverBankAccountEntity.isPresent()) {
            transactionBankAccountsEntity.setReceiverAccount(optionalReceiverBankAccountEntity.get());
        } else {
            throw new NoSuchElementException("Mapping error: receiver bank account entity is not found for id: " + dto.getReceiverAccountId());
        }
        return transactionBankAccountsEntity;
    }
}
