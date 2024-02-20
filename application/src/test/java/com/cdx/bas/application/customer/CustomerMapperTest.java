package com.cdx.bas.application.customer;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cdx.bas.domain.transaction.TransactionStatus.ERROR;
import static com.cdx.bas.domain.transaction.TransactionType.CREDIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class CustomerMapperTest {

    @Inject
    CustomerMapper customerMapper;
    
    @InjectMock
    private DtoEntityMapper<BankAccount, BankAccountEntity> bankAccountMapper;
    
    @Test
    public void toDto_shouldReturnNullDto_whenEntityIsNull() {
        Customer dto = customerMapper.toDto(null);
        
        assertThat(dto).isNull();
        
        verifyNoInteractions(bankAccountMapper);
    }

    @Test
    public void toEntity_shouldReturnNullEntity_whenDtoIsNull() {
        Customer dto = null;

        CustomerEntity entity = customerMapper.toEntity(dto);
        
        assertThat(entity).isNull();
        
        verifyNoInteractions(bankAccountMapper);
    }
    
	@Test
	public void toDto_shouldMapNullValues_whenEntityValuesNotDefined() throws JsonMappingException, JsonProcessingException {
        Customer dto = customerMapper.toDto(new CustomerEntity());
        
        assertThat(dto.getId()).isNull();
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
        assertThat(dto.getMetadata()).isEmpty();

        verifyNoInteractions(bankAccountMapper);
    }
    
    @Test
    public void toEntity_shouldMapNullValues_whenDtoValuesNotDefined() throws JsonProcessingException {
        CustomerEntity entity = customerMapper.toEntity(new Customer());
        
        assertThat(entity.getId()).isNull();
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
        assertThat(entity.getMetadata()).isNull();

        verifyNoInteractions(bankAccountMapper);
    }
    
	@Test
	public void toDto_shouldMapDtoValues_whenEntityHasValues() {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(1L);
        entity.setFirstName("Paul");
        entity.setLastName("Martin");
        entity.setGender(Gender.MALE);
        entity.setMaritalStatus(MaritalStatus.SINGLE);
        entity.setBirthdate(LocalDate.of(1995,Month.MAY,3));
        entity.setCountry("FR");
        entity.setAddress("100 avenue de la république");
        entity.setCity("Paris");
        entity.setEmail("jean.dupont@yahoo.fr");
        entity.setPhoneNumber("+33642645678");
        
        Instant instantDate = Instant.now();
        BankAccountEntity accountEntity1 = createBankAccountEntity(10L, instantDate);
        BankAccountEntity accountEntity2 = createBankAccountEntity(11L, instantDate);
        List<BankAccountEntity> accounts = new ArrayList<BankAccountEntity>();
        accounts.add(accountEntity1);
        accounts.add(accountEntity2);
        entity.setAccounts(accounts);
        
        String strMetadata = "{\"contact_preferences\" : \"email\", \"annual_salary\" : \"52000\"}";
        entity.setMetadata(strMetadata);
        
        BankAccount account1 = createBankAccount(10L, instantDate);
        BankAccount account2 = createBankAccount(11L, instantDate);
        when(bankAccountMapper.toDto(accountEntity1)).thenReturn(account1);
        when(bankAccountMapper.toDto(accountEntity2)).thenReturn(account2);
        
        Customer dto = customerMapper.toDto(entity);
        
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFirstName()).hasToString("Paul");
        assertThat(dto.getLastName()).hasToString("Martin");
        assertThat(dto.getGender()).isEqualTo(Gender.MALE);
        assertThat(dto.getBirthdate()).isBefore(LocalDate.now());
        assertThat(dto.getBirthdate().toString()).hasToString("1995-05-03");
        assertThat(dto.getCountry()).isEqualTo("FR");
        assertThat(dto.getMaritalStatus()).isEqualTo(MaritalStatus.SINGLE);
        assertThat(dto.getAddress()).hasToString("100 avenue de la république");
        assertThat(dto.getCity()).hasToString("Paris");
        assertThat(dto.getEmail()).hasToString("jean.dupont@yahoo.fr");
        assertThat(dto.getPhoneNumber()).hasToString("+33642645678");
        assertThat(dto.getAccounts()).hasSize(2);
        Set<BankAccount> accoutsToCompar = Stream.of(account1, account2).collect(Collectors.toSet());
        assertThat(dto.getAccounts()).containsExactlyInAnyOrderElementsOf(accoutsToCompar);
        assertThat(dto.getMetadata().get("contact_preferences")).isEqualTo("email");
        assertThat(dto.getMetadata().get("annual_salary")).isEqualTo("52000");
        
        verify(bankAccountMapper).toDto(accountEntity1);
        verify(bankAccountMapper).toDto(accountEntity2);
        verifyNoMoreInteractions(bankAccountMapper);
    }
    
    @Test
    public void toEntity_shouldMapEntityValues_whenDtoHasValues() {
        Customer model = new Customer();
        model.setId(1L);
        model.setFirstName("Paul");
        model.setLastName("Martin");
        model.setGender(Gender.MALE);
        model.setMaritalStatus(MaritalStatus.SINGLE);
        model.setBirthdate(LocalDate.of(1995,Month.MAY,3));
        model.setCountry("FR");
        model.setAddress("100 avenue de la république");
        model.setCity("Paris");
        model.setEmail("jean.dupont@yahoo.fr");
        model.setPhoneNumber("+33642645678");
        
        Instant instantDate = Instant.now();
        BankAccount account1 = createBankAccount(10L, instantDate);
        BankAccount account2 = createBankAccount(11L, instantDate);
        List<BankAccount> accounts = new ArrayList<BankAccount>();
        accounts.add(account1);
        accounts.add(account2);
        model.setAccounts(accounts);
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("contact_preferences", "email");
        metadata.put("annual_salary", "48000");
        model.setMetadata(metadata);
        
        BankAccountEntity accountEntity1 = createBankAccountEntity(10L, instantDate);
        BankAccountEntity accountEntity2 = createBankAccountEntity(11L, instantDate);
        when(bankAccountMapper.toEntity(account1)).thenReturn(accountEntity1);
        when(bankAccountMapper.toEntity(account2)).thenReturn(accountEntity2);
        
        CustomerEntity entity = customerMapper.toEntity(model);
        
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getFirstName()).hasToString("Paul");
        assertThat(entity.getLastName()).hasToString("Martin");
        assertThat(entity.getGender()).isEqualTo(Gender.MALE);
        assertThat(entity.getBirthdate()).isBefore(LocalDate.now());
        assertThat(entity.getBirthdate().toString()).hasToString("1995-05-03");
        assertThat(entity.getCountry()).isEqualTo("FR");
        assertThat(entity.getMaritalStatus()).isEqualTo(MaritalStatus.SINGLE);
        assertThat(entity.getAddress()).hasToString("100 avenue de la république");
        assertThat(entity.getCity()).hasToString("Paris");
        assertThat(entity.getEmail()).hasToString("jean.dupont@yahoo.fr");
        assertThat(entity.getPhoneNumber()).hasToString("+33642645678");
        assertThat(entity.getAccounts()).hasSize(2);
        Set<BankAccountEntity> accountsToCompar = Stream.of(accountEntity1, accountEntity2).collect(Collectors.toSet());
        assertThat(entity.getAccounts()).containsExactlyInAnyOrderElementsOf(accountsToCompar);
        assertThat(entity.getMetadata()).hasToString("{\"contact_preferences\":\"email\",\"annual_salary\":\"48000\"}");

        verify(bankAccountMapper).toEntity(account1);
        verify(bankAccountMapper).toEntity(account2);
        verifyNoMoreInteractions(bankAccountMapper);
    }
    
    private BankAccount createBankAccount(long accountId, Instant instantDate) {
        BankAccount bankAccount = new CheckingBankAccount();
        bankAccount.setId(accountId);
        bankAccount.setType(AccountType.CHECKING);
        bankAccount.setBalance(new Money(new BigDecimal("100")));
        List<Long> customersId = new ArrayList<>();
        customersId.add(99L);
        bankAccount.setCustomersId(customersId);
        Set<Transaction> transactions = new HashSet<>();
        transactions.add(createTransaction(99L, accountId, instantDate));
        transactions.add(createTransaction(100L, accountId, instantDate));
        transactions.add(createTransaction(100L, accountId, instantDate));
        bankAccount.setIssuedTransactions(transactions);
        return bankAccount;
    }
    
    private BankAccountEntity createBankAccountEntity(long id, Instant instantDate) {
        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setId(id);
        bankAccountEntity.setType(AccountType.CHECKING);
        bankAccountEntity.setBalance(new BigDecimal("100"));
        List<CustomerEntity> customersId = new ArrayList<>();
        customersId.add(new CustomerEntity());
        bankAccountEntity.setCustomers(customersId);
        HashSet<TransactionEntity> transactionEntities = new HashSet<>();
        transactionEntities.add(createTransactionEntity(99L, instantDate));
        transactionEntities.add(createTransactionEntity(100L, instantDate));
        bankAccountEntity.setIssuedTransactions(transactionEntities);
        return bankAccountEntity;
    }

    private Transaction createTransaction(long id, long senderAccountId, Instant instantDate) {
        return Transaction.builder()
                .id(id)
                .type(CREDIT)
                .senderAccountId(senderAccountId)
                .receiverAccountId(77L)
                .amount(new BigDecimal("100"))
                .status(ERROR)
                .date(instantDate)
                .label("transaction test")
                .build();
    }
    
    private TransactionEntity createTransactionEntity(long id, Instant instantDate) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(id);
        transactionEntity.setSenderBankAccountEntity(null);
        transactionEntity.setReceiverBankAccountEntity(null);
        transactionEntity.setAmount(new BigDecimal("100"));
        transactionEntity.setType(CREDIT);
        transactionEntity.setStatus(ERROR);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        return transactionEntity;
    }
}
