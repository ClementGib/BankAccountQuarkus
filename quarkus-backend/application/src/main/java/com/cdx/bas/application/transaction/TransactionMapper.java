package com.cdx.bas.application.transaction;

import javax.enterprise.context.RequestScoped;

import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.transaction.Transaction;

@RequestScoped
public class TransactionMapper implements DtoEntityMapper<Transaction, TransactionEntity> {
    
    public Transaction toDto(TransactionEntity entity) {        		
        		Transaction dto = new Transaction();
                dto.setId(entity.getId());
//                entity.getAccount().getId().longValue(), 
//                entity.getAmount().longValue(), 
//                TransactionType.valueOf(entity.getType()),
//                TransactionStatus.valueOf(entity.getStatus()), 
//                entity.getDate(), 
//                entity.getLabel()
                return dto;
    }

    public TransactionEntity toEntity(Transaction model) {
        TransactionEntity entity = new TransactionEntity();
//        entity.setId(model.accountId());
//        entity.setAccount(model.accountId());
//        entity.setIntroduction(model.introduction());
//        entity.setDescription(model.description());
//        entity.setCreationDate(model.creationDate());
//        entity.setLikeCount(model.likeCount());
//        entity.setImageId(model.imageId());
        return entity;
    }
}

//private Long id;
//private BankAccountEntity account;
//private String type;
//private BigDecimal amount;
//private String status;
//private Instant date;
//private String label;