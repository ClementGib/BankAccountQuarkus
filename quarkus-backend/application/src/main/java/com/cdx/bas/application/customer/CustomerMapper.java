package com.cdx.bas.application.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.customer.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.MappingException;

@RequestScoped
public class CustomerMapper implements DtoEntityMapper<Customer, CustomerEntity> {
    
    @Inject
    DtoEntityMapper<BankAccount, BankAccountEntity> bankAccountMapper;
    
    @Inject
    ObjectMapper objectMapper;
    
    @Override
    public Customer toDto(CustomerEntity entity) {
        
        if (entity == null) {
            return null;
        }
        
        Customer dto = new Customer();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setGender(entity.getGender());
        dto.setMaritalStatus(entity.getMaritalStatus());
        dto.setBirthdate(entity.getBirthdate());
        dto.setCountry(entity.getCountry());
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAccounts(entity.getAccounts().stream()
        		.map(bankAccountMapper::toDto).collect(Collectors.toList()));
        
        try {
            if (entity.getMetadata() != null) {
                dto.setMetadata(objectMapper.readValue(entity.getMetadata(), new TypeReference<Map<String, String>>() {}));
            } else {
                dto.setMetadata(new HashMap<>());
            }
        } catch (JsonProcessingException exception) {
            throw new MappingException("An error occured while parsing JSON String to Map<String, String>", exception);
        }
        return dto;
    }

    @Override
    public CustomerEntity toEntity(Customer dto) {
        
        if (dto == null) {
            return null;
        }
        
        CustomerEntity entity = new CustomerEntity();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setGender(dto.getGender());
        entity.setMaritalStatus(dto.getMaritalStatus());
        entity.setBirthdate(dto.getBirthdate());
        entity.setCountry(dto.getCountry());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAccounts(dto.getAccounts().stream()
        		.map(bankAccountMapper::toEntity).collect(Collectors.toList()));
        
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
