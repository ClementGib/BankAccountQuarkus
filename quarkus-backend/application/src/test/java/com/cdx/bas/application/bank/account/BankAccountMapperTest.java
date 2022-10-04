package com.cdx.bas.application.bank.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

import javax.inject.Inject;

import com.cdx.bas.application.customer.CustomerEntity;
import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.customer.Customer;
import com.cdx.bas.domain.money.Money;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class BankAccountMapperTest {
	
    @Inject
    BankAccountMapper bankAccountMapper;
    
    @InjectMock
    private DtoEntityMapper<Customer, CustomerEntity> customerMapper;
    
    @Test
    public void toDto_should_returnNullDto_when_entityIsNull() {
        BankAccountEntity entity = null;

        BankAccount dto = bankAccountMapper.toDto(entity);
        
        assertThat(dto).isNull();
        
        verifyNoInteractions(customerMapper);
    }

    @Test
    public void toEntity_should_returnNullEntity_when_dtoIsNull() {
    	BankAccount dto = null;

    	BankAccountEntity entity = bankAccountMapper.toEntity(dto);
        
        assertThat(entity).isNull();
        
        verifyNoInteractions(customerMapper);
    }
    
	@Test
	public void toDto_should_mapNullValues_when_entityHasNullValues() {
        BankAccount dto = bankAccountMapper.toDto(new BankAccountEntity());
        
        assertThat(dto.getId()).isZero();
        assertThat(dto.getType()).isNull();
        assertThat(dto.getBalance()).usingRecursiveComparison().isEqualTo(new Money(null));
        assertThat(dto.getCustomersId()).isEmpty();
        assertThat(dto.getTransactions()).isEmpty();
        assertThat(dto.getHistory()).isEmpty();

        verifyNoInteractions(customerMapper);
    }
    
    @Test
    public void toEntity_should_mapNullValues_when_dtoHasNullValues() {
        BankAccountEntity entity = bankAccountMapper.toEntity(new CheckingBankAccount());
        
        assertThat(entity.getId()).isZero();
        assertThat(entity.getType()).isNull();
        assertThat(entity.getBalance()).isNull();
        assertThat(entity.getCustomers()).isEmpty();
        assertThat(entity.getTransactions()).isEmpty();
        assertThat(entity.getHistory()).isEmpty();

        verifyNoInteractions(customerMapper);
    }
}
