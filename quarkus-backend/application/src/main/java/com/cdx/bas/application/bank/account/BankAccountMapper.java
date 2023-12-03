package com.cdx.bas.application.bank.account;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import com.cdx.bas.application.customer.CustomerEntity;
import com.cdx.bas.application.customer.CustomerRepository;
import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.application.transaction.TransactionEntity;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.utils.BankAccountFactory;

@RequestScoped
public class BankAccountMapper implements DtoEntityMapper<BankAccount, BankAccountEntity> {

    @Inject
    private CustomerRepository customerRepository;
    @Inject
    private BankAccountRepository bankAccountRepository;
    @Inject
    private DtoEntityMapper<Transaction, TransactionEntity> transactionMapper;

    @Override
    public BankAccount toDto(BankAccountEntity entity) {

        if (entity == null || entity.getType() == null) {
            return null;
        }

        BankAccount dto = BankAccountFactory.createBankAccount(entity.getType());
        dto.setId(entity.getId());
        dto.setBalance(new Money(entity.getBalance()));

        dto.setCustomersId(entity.getCustomers().stream()
                .map(CustomerEntity::getId)
                .collect(Collectors.toList()));
        dto.setTransactions(entity.getTransactions().stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public BankAccountEntity toEntity(BankAccount dto) {

        if (dto == null || dto.getId() == null) {
            return null;
        }

        BankAccountEntity entity = bankAccountRepository.findByIdOptional(dto.getId()).orElse(new BankAccountEntity());
        entity.setId(dto.getId());
        entity.setType(dto.getType());

        if (dto.getBalance() != null) {
            entity.setBalance(dto.getBalance().getAmount());
        } else {
            entity.setBalance(null);
        }

        entity.setCustomers(dto.getCustomersId().stream()
                .map(customerId -> customerRepository.findByIdOptional(customerId)
                        .orElseThrow(() -> new NoSuchElementException("Customer entity not found for id: " + customerId)))
                .collect(Collectors.toList()));

        entity.setTransactions(dto.getTransactions().stream()
                .map(transactionMapper::toEntity)
                .collect(Collectors.toSet()));
        return entity;
    }
}
