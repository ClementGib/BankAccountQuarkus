package com.cdx.bas.application.bank.transaction;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.application.bank.account.BankAccountRepository;
import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.hibernate.MappingException;

import java.util.HashMap;
import java.util.NoSuchElementException;

@RequestScoped
public class TransactionMapper implements DtoEntityMapper<Transaction, TransactionEntity> {

    @Inject
    TransactionRepository transactionRepository;

    @Inject
    BankAccountRepository bankAccountRepository;

    @Inject
    ObjectMapper objectMapper;

    public Transaction toDto(TransactionEntity entity) {
        Transaction dto = Transaction.builder()
                .id(entity.getId())
                .type(entity.getType())
                .currency(entity.getCurrency())
                .status(entity.getStatus())
                .date(entity.getDate())
                .label(entity.getLabel())
                .build();

        if (entity.getEmitterBankAccountEntity() != null) {
            dto.setEmitterAccountId(entity.getEmitterBankAccountEntity().getId());
        } else {
            throw new NoSuchElementException("Transaction does not have emitter bank account.");
        }

        if (entity.getReceiverBankAccountEntity() != null) {
            dto.setReceiverAccountId(entity.getReceiverBankAccountEntity().getId());
        } else {
            throw new NoSuchElementException("Transaction does not have receiver bank account.");
        }

        if (entity.getAmount() != null) {
            dto.setAmount(entity.getAmount());
        }

        try {
            if (entity.getMetadata() != null) {
                dto.setMetadata(objectMapper.readValue(entity.getMetadata(), new TypeReference<HashMap<String, String>>() {}));
            } else {
                dto.setMetadata(new HashMap<>());
            }
        } catch (JsonProcessingException exception) {
            throw new MappingException("Mapping error: An error occurred while parsing JSON String to Map<String, String>", exception);
        }
        return dto;
    }

    public TransactionEntity toEntity(Transaction dto) {
        TransactionEntity entity ;
        if (dto.getId() == null) {
            entity = new TransactionEntity();
        } else {
            entity = transactionRepository.findByIdOptional(dto.getId()).orElse(new TransactionEntity());
            entity.setId(dto.getId());
        }

        BankAccountEntity emitterBankAccountEntity = bankAccountRepository.findByIdOptional(dto.getEmitterAccountId())
                .orElseThrow(() -> new NoSuchElementException("Transaction does not have emitter bank account entity."));

        BankAccountEntity receiverBankAccountEntity = bankAccountRepository.findByIdOptional(dto.getReceiverAccountId())
                .orElseThrow(() -> new NoSuchElementException("Transaction does not have receiver bank account entity."));

        entity.setEmitterBankAccountEntity(emitterBankAccountEntity);
        entity.setReceiverBankAccountEntity(receiverBankAccountEntity);
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
