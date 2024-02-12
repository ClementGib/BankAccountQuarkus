package com.cdx.bas.application.bank.account;

import com.cdx.bas.application.customer.CustomerEntity;
import com.cdx.bas.application.customer.CustomerRepository;
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
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class BankAccountMapperTest {

    @Inject
    BankAccountMapper bankAccountMapper;

    @InjectMock
    private DtoEntityMapper<Customer, CustomerEntity> customerMapper;

    @InjectMock
    private DtoEntityMapper<Transaction, TransactionEntity> transactionMapper;

    @InjectMock
    private CustomerRepository customerRepository;
    
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
    public void toDto_should_returnNullDto_when_entityHasNullTypeValues() {
        BankAccount dto = bankAccountMapper.toDto(new BankAccountEntity());

        assertThat(dto).isNull();

        verifyNoInteractions(customerMapper);
    }

    @Test
    public void toDto_should_mapNullValues_when_entityHasNullValuesButHasType() {
        BankAccountEntity entity = new BankAccountEntity();
        entity.setType(AccountType.CHECKING);

        BankAccount dto = bankAccountMapper.toDto(entity);

        assertThat(dto.getId()).isNull();
        assertThat(dto.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(dto.getBalance()).usingRecursiveComparison().isEqualTo(new Money(null));
        assertThat(dto.getCustomersId()).isEmpty();
        assertThat(dto.getIssuedTransactions()).isEmpty();

        verifyNoInteractions(customerMapper);
    }

    @Test
        public void toEntity_should_mapNullValues_when_dtoHasNullValuesButHasTypeAndId() {
        BankAccount bankAccount = new CheckingBankAccount();
        bankAccount.setId(1L);
        BankAccountEntity entity = bankAccountMapper.toEntity(bankAccount);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(entity.getBalance()).isNull();
        assertThat(entity.getCustomers()).isEmpty();
        assertThat(entity.getIssuedTransactions()).isEmpty();

        verifyNoInteractions(customerMapper);
    }

    @Test
    public void toDto_should_mapDtoValues_when_entityHasValues() {
        Instant date = Instant.now();
        BankAccountEntity entity = new BankAccountEntity();
        entity.setId(10L);
        entity.setType(AccountType.CHECKING);
        entity.setBalance(new BigDecimal("1000"));
        List<CustomerEntity> customers = new ArrayList<>();
        CustomerEntity customerEntity = createCustomerEntity();
        customers.add(customerEntity);
        entity.setCustomers(customers);
        Set<TransactionEntity> transactionEntities = new HashSet<>();
        TransactionEntity transactionEntity1 = createTransactionEntity(2000L, 10L, date);
        transactionEntities.add(transactionEntity1);
        TransactionEntity transactionEntity2 = createTransactionEntity(5000L, 10L, date);
        transactionEntities.add(transactionEntity2);
        entity.setIssuedTransactions(transactionEntities);

        Transaction transaction1 = createTransaction(2000L, 10L, date);
        Transaction transaction2 = createTransaction(5000L, 10L, date);
        when(transactionMapper.toDto(transactionEntity1)).thenReturn(transaction1);
        when(transactionMapper.toDto(transactionEntity2)).thenReturn(transaction2);
        BankAccount dto = bankAccountMapper.toDto(entity);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(dto.getBalance()).usingRecursiveComparison().isEqualTo(new Money(new BigDecimal("1000")));
        assertThat(dto.getCustomersId()).hasSize(1);
        assertThat(dto.getCustomersId().iterator().next()).isEqualTo(99L);
        assertThat(dto.getIssuedTransactions()).hasSize(2);
        assertThat(dto.getIssuedTransactions()).contains(transaction1);
        assertThat(dto.getIssuedTransactions()).contains(transaction2);

        verify(transactionMapper).toDto(transactionEntity1);
        verify(transactionMapper).toDto(transactionEntity2);
        verifyNoMoreInteractions(customerMapper, transactionMapper);
    }

    @Test
    public void toDto_should_mapThrowNoSuchElementException_when_customerIsMissing() {
        Instant date = Instant.now();
        BankAccount dto = new CheckingBankAccount();
        dto.setId(10L);
        dto.setType(AccountType.CHECKING);
        dto.setBalance(new Money(new BigDecimal("1000")));
        List<Long> customers = new ArrayList<>();
        Customer customer = createCustomer();
        customers.add(customer.getId());
        dto.setCustomersId(customers);
        Set<Transaction> transactions = new HashSet<>();
        Transaction transaction1 = createTransaction(2000L, 10L, date);
        transactions.add(transaction1);
        Transaction transaction2 = createTransaction(5000L, 10L, date);
        transactions.add(transaction2);
        dto.setIssuedTransactions(transactions);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            bankAccountMapper.toEntity(dto);
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Customer entity not found for id: 99");
        }

        verify(customerRepository).findByIdOptional(customer.getId());
        verifyNoInteractions(customerMapper, transactionMapper);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    public void toEntity_should_mapDtoValues_when_dtoHasValues() {
        Instant date = Instant.now();
        BankAccount dto = new CheckingBankAccount();
        dto.setId(10L);
        dto.setType(AccountType.CHECKING);
        dto.setBalance(new Money(new BigDecimal("1000")));
        List<Long> customers = new ArrayList<>();
        Customer customer = createCustomer();
        customers.add(customer.getId());
        dto.setCustomersId(customers);
        Set<Transaction> transactions = new HashSet<>();
        Transaction transaction1 = createTransaction(2000L, 10L, date);
        transactions.add(transaction1);
        Transaction transaction2 = createTransaction(5000L, 10L, date);
        transactions.add(transaction2);
        dto.setIssuedTransactions(transactions);

        CustomerEntity customerEntity = createCustomerEntity();
        when(customerRepository.findByIdOptional(anyLong())).thenReturn(Optional.of(customerEntity));
        when(customerMapper.toEntity(customer)).thenReturn(customerEntity);
        TransactionEntity transactionEntity1 = createTransactionEntity(2000L, 10L, date);
        when(transactionMapper.toEntity(transaction1)).thenReturn(transactionEntity1);
        TransactionEntity transactionEntity2 = createTransactionEntity(5000L, 10L, date);
        when(transactionMapper.toEntity(transaction2)).thenReturn(transactionEntity2);


        BankAccountEntity entity = bankAccountMapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(10L);
        assertThat(entity.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(entity.getBalance()).usingRecursiveComparison().isEqualTo(new BigDecimal("1000"));
        assertThat(entity.getCustomers()).hasSize(1);
        assertThat(entity.getCustomers().iterator().next()).isEqualTo(customerEntity);
        assertThat(entity.getIssuedTransactions()).hasSize(2);
        assertThat(entity.getIssuedTransactions()).contains(transactionEntity1);
        assertThat(entity.getIssuedTransactions()).contains(transactionEntity2);

        verify(customerRepository).findByIdOptional(customer.getId());
        verify(transactionMapper).toEntity(transaction1);
        verify(transactionMapper).toEntity(transaction2);
        verifyNoMoreInteractions(customerMapper, transactionMapper, customerRepository);
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setId(99L);
        customer.setFirstName("Paul");
        customer.setLastName("Martin");
        customer.setGender(Gender.MALE);
        customer.setMaritalStatus(MaritalStatus.SINGLE);
        customer.setBirthdate(LocalDate.of(1995, Month.MAY, 3));
        customer.setCountry("FR");
        customer.setAddress("100 avenue de la république");
        customer.setCity("Paris");
        customer.setEmail("jean.dupont@yahoo.fr");
        customer.setPhoneNumber("+33642645678");
        return customer;
    }

    private CustomerEntity createCustomerEntity() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(99L);
        customerEntity.setFirstName("Paul");
        customerEntity.setLastName("Martin");
        customerEntity.setGender(Gender.MALE);
        customerEntity.setMaritalStatus(MaritalStatus.SINGLE);
        customerEntity.setBirthdate(LocalDate.of(1995, Month.MAY, 3));
        customerEntity.setCountry("FR");
        customerEntity.setAddress("100 avenue de la république");
        customerEntity.setCity("Paris");
        customerEntity.setEmail("jean.dupont@yahoo.fr");
        customerEntity.setPhoneNumber("+33642645678");
        return customerEntity;
    }

    private Transaction createTransaction(long id, long accountId, Instant instantDate) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setSenderAccountId(accountId);
        transaction.setReceiverAccountId(77L);
        transaction.setAmount(new BigDecimal(100.00));
        transaction.setType(TransactionType.CREDIT);
        transaction.setStatus(TransactionStatus.ERROR);
        transaction.setDate(instantDate);
        transaction.setLabel("transaction test");
        return transaction;
    }

    private TransactionEntity createTransactionEntity(long id, long accountId, Instant instantDate) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(id);
        transactionEntity.setSenderBankAccountEntity(null);
        transactionEntity.setReceiverBankAccountEntity(null);
        transactionEntity.setAmount(new BigDecimal("100"));
        transactionEntity.setType(TransactionType.CREDIT);
        transactionEntity.setStatus(TransactionStatus.ERROR);
        transactionEntity.setDate(instantDate);
        transactionEntity.setLabel("transaction test");
        return transactionEntity;
    }
}
