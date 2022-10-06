package com.cdx.bas.application.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.application.transaction.TransactionEntity;
import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.customer.Customer;
import com.cdx.bas.domain.customer.Gender;
import com.cdx.bas.domain.customer.MaritalStatus;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class CustomerMapperTest {

    @Inject
    CustomerMapper customerMapper;
    
    @InjectMock
    private DtoEntityMapper<BankAccount, BankAccountEntity> bankAccountMapper;
    
    @InjectMock
    private ObjectMapper objectMapper;
    
    @Test
    public void toDto_should_returnNullDto_when_entityIsNull() {
        CustomerEntity entity = null;

        Customer dto = customerMapper.toDto(entity);
        
        assertThat(dto).isNull();
        
        verifyNoInteractions(bankAccountMapper, objectMapper);
    }

    @Test
    public void toEntity_should_returnNullEntity_when_dtoIsNull() {
        Customer dto = null;

        CustomerEntity entity = customerMapper.toEntity(dto);
        
        assertThat(entity).isNull();
        
        verifyNoInteractions(bankAccountMapper, objectMapper);
    }
    
	@Test
	public void toDto_should_mapNullValues_when_entityValuesNotDefined() throws JsonMappingException, JsonProcessingException {
        Customer dto = customerMapper.toDto(new CustomerEntity());
        
        assertThat(dto.getId()).isZero();
        assertThat(dto.getFirstName()).isNull();
        assertThat(dto.getLastName()).isNull();
        assertThat(dto.getGender()).isNull();
        assertThat(dto.getBirthdate()).isNull();
        assertThat(dto.getBirthdate()).isNull();
        assertThat(dto.getCountry()).isNull();
        assertThat(dto.getMaritalStatus()).isNull();
        assertThat(dto.getAddress()).isNull();
        assertThat(dto.getCity()).isNull();
        assertThat(dto.getEmail()).isNull();
        assertThat(dto.getPhoneNumber()).isNull();
        assertThat(dto.getAccounts()).isEmpty();
        assertThat(dto.getMetadatas()).isEmpty();

        verifyNoInteractions(bankAccountMapper, objectMapper);
    }
    
    @Test
    public void toEntity_should_mapNullValues_when_dtoValuesNotDefined() throws JsonProcessingException {
        CustomerEntity entity = customerMapper.toEntity(new Customer());
        
        assertThat(entity.getId()).isZero();
        assertThat(entity.getFirstName()).isNull();
        assertThat(entity.getLastName()).isNull();
        assertThat(entity.getGender()).isNull();
        assertThat(entity.getBirthdate()).isNull();
        assertThat(entity.getBirthdate()).isNull();
        assertThat(entity.getCountry()).isNull();
        assertThat(entity.getMaritalStatus()).isNull();
        assertThat(entity.getAddress()).isNull();
        assertThat(entity.getCity()).isNull();
        assertThat(entity.getEmail()).isNull();
        assertThat(entity.getPhoneNumber()).isNull();
        assertThat(entity.getAccounts()).isEmpty();
        assertThat(entity.getMetadatas()).isNull();

        verifyNoInteractions(bankAccountMapper, objectMapper);
    }
    
	@Test
	@SuppressWarnings("unchecked")
	public void toDto_should_mapDtoValues_when_entityHasValues() throws JsonMappingException, JsonProcessingException {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(1L);
        entity.setFirstName("Paul");
        entity.setLastName("Martin");
        entity.setGender(Gender.MALE);
        entity.setMaritalStatus(MaritalStatus.SINGLE);
        entity.setBirthdate(LocalDateTime.of(1995,Month.MAY,3,6,30,40,50000));
        entity.setCountry("FR");
        entity.setAddress("100 avenue de la république");
        entity.setCity("Paris");
        entity.setEmail("jean.dupont@yahoo.fr");
        entity.setPhoneNumber("+33642645678");
        
        Instant instantDate = Instant.now();
        BankAccountEntity accountEntity1 = createBankAccountEntity(10L, instantDate);
        BankAccountEntity accountEntity2 = createBankAccountEntity(11L, instantDate);
        HashSet<BankAccountEntity> accounts = new HashSet<BankAccountEntity>();
        accounts.add(accountEntity1);
        accounts.add(accountEntity2);
        entity.setAccounts(accounts);
        
        String strMetadatas = "{\"contact_preferences\" : \"email\", \"annual_salary\" : \"52000\"}";
        entity.setMetadatas(strMetadatas);
        
        BankAccount account1 = createBankAccount(10L, instantDate);
        BankAccount account2 = createBankAccount(11L, instantDate);
        when(bankAccountMapper.toDto(accountEntity1)).thenReturn(account1);
        when(bankAccountMapper.toDto(accountEntity2)).thenReturn(account2);
        HashMap<String, String> metadatas = new HashMap<String, String>();
        metadatas.put("contact_preferences", "email");
        metadatas.put("annual_salary", "52000");
        when(objectMapper.readValue(eq(strMetadatas), any(TypeReference.class))).thenReturn(metadatas);
        
        Customer dto = customerMapper.toDto(entity);
        
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFirstName()).hasToString("Paul");
        assertThat(dto.getLastName()).hasToString("Martin");
        assertThat(dto.getGender()).isEqualTo(Gender.MALE);
        assertThat(dto.getBirthdate()).isBefore(LocalDateTime.now());
        assertThat(dto.getBirthdate().toString()).hasToString("1995-05-03T06:30:40.000050");
        assertThat(dto.getCountry()).isEqualTo("FR");
        assertThat(dto.getMaritalStatus()).isEqualTo(MaritalStatus.SINGLE);
        assertThat(dto.getAddress()).hasToString("100 avenue de la république");
        assertThat(dto.getCity()).hasToString("Paris");
        assertThat(dto.getEmail()).hasToString("jean.dupont@yahoo.fr");
        assertThat(dto.getPhoneNumber()).hasToString("+33642645678");
        assertThat(dto.getAccounts()).hasSize(2);
        Set<BankAccount> accoutsToCompar = Stream.of(account1, account2).collect(Collectors.toSet());
        assertThat(dto.getAccounts()).containsExactlyInAnyOrderElementsOf(accoutsToCompar);
        assertThat(dto.getMetadatas().get("contact_preferences")).isEqualTo("email");
        assertThat(dto.getMetadatas().get("annual_salary")).isEqualTo("52000");
        
        verify(bankAccountMapper).toDto(accountEntity1);
        verify(bankAccountMapper).toDto(accountEntity2);
        verify(objectMapper).readValue(eq(strMetadatas), any(TypeReference.class));
        verifyNoMoreInteractions(bankAccountMapper, objectMapper);
    }
    
    @Test
    public void toEntity_should_mapEntityValues_when_dtoHasValues() throws JsonProcessingException {
        Customer model = new Customer();
        model.setId(1L);
        model.setFirstName("Paul");
        model.setLastName("Martin");
        model.setGender(Gender.MALE);
        model.setMaritalStatus(MaritalStatus.SINGLE);
        model.setBirthdate(LocalDateTime.of(1995,Month.MAY,3,6,30,40,50000));
        model.setCountry("FR");
        model.setAddress("100 avenue de la république");
        model.setCity("Paris");
        model.setEmail("jean.dupont@yahoo.fr");
        model.setPhoneNumber("+33642645678");
        
        Instant instantDate = Instant.now();
        BankAccount account1 = createBankAccount(10L, instantDate);
        BankAccount account2 = createBankAccount(11L, instantDate);
        HashSet<BankAccount> accounts = new HashSet<BankAccount>();
        accounts.add(account1);
        accounts.add(account2);
        model.setAccounts(accounts);
        
        HashMap<String, String> metadatas = new HashMap<String, String>();
        metadatas.put("contact_preferences", "email");
        metadatas.put("annual_salary", "48000");
        model.setMetadatas(metadatas);
        
        BankAccountEntity accountEntity1 = createBankAccountEntity(10L, instantDate);
        BankAccountEntity accountEntity2 = createBankAccountEntity(11L, instantDate);
        when(bankAccountMapper.toEntity(account1)).thenReturn(accountEntity1);
        when(bankAccountMapper.toEntity(account2)).thenReturn(accountEntity2);
        when(objectMapper.writeValueAsString(metadatas)).thenReturn("{\"contact_preferences\" : \"email\", \"annual_salary\" : \"52000\"}");
        
        CustomerEntity entity = customerMapper.toEntity(model);
        
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getFirstName()).hasToString("Paul");
        assertThat(entity.getLastName()).hasToString("Martin");
        assertThat(entity.getGender()).isEqualTo(Gender.MALE);
        assertThat(entity.getBirthdate()).isBefore(LocalDateTime.now());
        assertThat(entity.getBirthdate().toString()).hasToString("1995-05-03T06:30:40.000050");
        assertThat(entity.getCountry()).isEqualTo("FR");
        assertThat(entity.getMaritalStatus()).isEqualTo(MaritalStatus.SINGLE);
        assertThat(entity.getAddress()).hasToString("100 avenue de la république");
        assertThat(entity.getCity()).hasToString("Paris");
        assertThat(entity.getEmail()).hasToString("jean.dupont@yahoo.fr");
        assertThat(entity.getPhoneNumber()).hasToString("+33642645678");
        assertThat(entity.getAccounts()).hasSize(2);
        Set<BankAccountEntity> accoutsToCompar = Stream.of(accountEntity1, accountEntity2).collect(Collectors.toSet());
        assertThat(entity.getAccounts()).containsExactlyInAnyOrderElementsOf(accoutsToCompar);
        assertThat(entity.getMetadatas()).hasToString("{\"contact_preferences\" : \"email\", \"annual_salary\" : \"52000\"}");
        
        verify(bankAccountMapper).toEntity(account1);
        verify(bankAccountMapper).toEntity(account2);
        verify(objectMapper).writeValueAsString(metadatas);
        verifyNoMoreInteractions(bankAccountMapper, objectMapper);
    }
    
    private BankAccount createBankAccount(long accountId, Instant instantDate) {
        BankAccount bankAccount = new CheckingBankAccount();
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(new Money(new BigDecimal("100")));
        Set<Long> customersId = new HashSet<>();
        customersId.add(99L);
        bankAccount.setCustomersId(customersId);
        Set<Transaction> transactions = new HashSet<>();
        transactions.add(createTransaction(100L, accountId, instantDate));
        bankAccount.setTransactions(transactions);
        HashSet<Transaction> history = new HashSet<>();
        history.add(createTransaction(99L, accountId, instantDate));
        history.add(createTransaction(100L, accountId, instantDate));
        bankAccount.setHistory(history);
        return bankAccount;
    }
    
    private BankAccountEntity createBankAccountEntity(long id, Instant instantDate) {
        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setId(id);
        bankAccountEntity.setType(AccountType.CHECKING);
        bankAccountEntity.setBalance(new BigDecimal("100"));
        HashSet<Long> customersId = new HashSet<>();
        customersId.add(99L);
        bankAccountEntity.setCustomers(new HashSet<>());
        HashSet<TransactionEntity> transactionEntities = new HashSet<>();
        transactionEntities.add(createTransactionEntity(100L, id, instantDate));
        bankAccountEntity.setTransactions(transactionEntities);
        HashSet<TransactionEntity> historyEntities = new HashSet<>();
        historyEntities.add(createTransactionEntity(99L, id, instantDate));
        bankAccountEntity.setHistory(historyEntities);
        return bankAccountEntity;
    }
    
    private Transaction createTransaction(long id, long accountId, Instant instantDate) {
        Transaction transactionEntity = new Transaction();
        transactionEntity.setId(id);
        transactionEntity.setAccountId(accountId);
        transactionEntity.setAmount(100L);
        transactionEntity.setType(TransactionType.CREDIT);
        transactionEntity.setStatus(TransactionStatus.ERROR);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        return transactionEntity;
    }
    
    private TransactionEntity createTransactionEntity(long id, long accountId, Instant instantDate) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(id);
        transactionEntity.setAccount(null);
        transactionEntity.setAmount(new BigDecimal("100"));
        transactionEntity.setType(TransactionType.CREDIT);
        transactionEntity.setStatus(TransactionStatus.ERROR);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        return transactionEntity;
    }
}
