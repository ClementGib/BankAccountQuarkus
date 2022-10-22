package com.cdx.bas.application.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

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
    private DtoEntityMapper<BankAccount, BankAccountEntity> bankAccountMapper;
    
    @Inject
    private ObjectMapper objectMapper;
    
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
        		.map(bankAccountMapper::toDto).toList());
        
        try {
            if (entity.getMetadatas() != null) {
                dto.setMetadatas(objectMapper.readValue(entity.getMetadatas(), new TypeReference<Map<String, String>>() {}));
            } else {
                dto.setMetadatas(new HashMap<>());
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
        		.map(bankAccountMapper::toEntity).toList());
        
        try {
            if (!dto.getMetadatas().isEmpty()) {
                entity.setMetadatas(objectMapper.writeValueAsString(dto.getMetadatas()));
            } else {
                entity.setMetadatas(null);
            }
        } catch (JsonProcessingException exception) {
            throw new MappingException("An error occured while parsing Map<String, String> to JSON String", exception);
        }
        return entity;
    }
}
