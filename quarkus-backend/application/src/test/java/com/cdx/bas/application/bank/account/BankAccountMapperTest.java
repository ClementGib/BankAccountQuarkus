package com.cdx.bas.application.bank.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import com.cdx.bas.application.customer.CustomerEntity;
import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.application.transaction.TransactionEntity;
import com.cdx.bas.domain.bank.account.AccountType;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.checking.CheckingBankAccount;
import com.cdx.bas.domain.customer.Customer;
import com.cdx.bas.domain.customer.CustomerPersistencePort;
import com.cdx.bas.domain.customer.Gender;
import com.cdx.bas.domain.customer.MaritalStatus;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionStatus;
import com.cdx.bas.domain.transaction.TransactionType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class BankAccountMapperTest {

    @Inject
    BankAccountMapper bankAccountMapper;

    @InjectMock
    private DtoEntityMapper<Customer, CustomerEntity> customerMapper;

    @InjectMock
    private DtoEntityMapper<Transaction, TransactionEntity> transactionMapper;

    @InjectMock
    private CustomerPersistencePort customerRepository;

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

        assertThat(dto.getId()).isZero();
        assertThat(dto.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(dto.getBalance()).usingRecursiveComparison().isEqualTo(new Money(null));
        assertThat(dto.getCustomersId()).isEmpty();
        assertThat(dto.getTransactions()).isEmpty();
        assertThat(dto.getHistory()).isEmpty();

        verifyNoInteractions(customerMapper);
    }

    @Test
    public void toEntity_should_mapNullValues_when_dtoHasNullValuesButHasType() {
        BankAccountEntity entity = bankAccountMapper.toEntity(new CheckingBankAccount());

        assertThat(entity.getId()).isZero();
        assertThat(entity.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(entity.getBalance()).isNull();
        assertThat(entity.getCustomers()).isEmpty();
        assertThat(entity.getTransactions()).isEmpty();
        assertThat(entity.getHistory()).isEmpty();

        verifyNoInteractions(customerMapper);
    }

    @Test
    public void toDto_should_mapDtoValues_when_entityHasValues() {
        Instant date = Instant.now();
        BankAccountEntity entity = new BankAccountEntity();
        entity.setId(10L);
        entity.setType(AccountType.CHECKING);
        entity.setBalance(new BigDecimal("1000"));
        Set<CustomerEntity> customers = new HashSet<>();
        CustomerEntity customerEntity = createCustomerEntity();
        customers.add(customerEntity);
        entity.setCustomers(customers);
        Set<TransactionEntity> transactionEntities = new HashSet<>();
        TransactionEntity transactionEntity = createTransactionEntity(5000L, 10L, date);
        transactionEntities.add(transactionEntity);
        entity.setTransactions(transactionEntities);
        Set<TransactionEntity> historyEntities = new HashSet<>();
        TransactionEntity historyEntity = createTransactionEntity(2000L, 10L, date);
        historyEntities.add(historyEntity);
        entity.setHistory(historyEntities);

        Customer customer = createCustomer();
        when(customerMapper.toDto(customerEntity)).thenReturn(customer);
        Transaction transaction = createTransaction(5000L, 10L, date);
        when(transactionMapper.toDto(transactionEntity)).thenReturn(transaction);
        Transaction history = createTransaction(2000L, 10L, date);
        when(transactionMapper.toDto(historyEntity)).thenReturn(history);
        BankAccount dto = bankAccountMapper.toDto(entity);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(dto.getBalance()).usingRecursiveComparison().isEqualTo(new Money(new BigDecimal("1000")));
        assertThat(dto.getCustomersId()).hasSize(1);
        assertThat(dto.getCustomersId().iterator().next()).isEqualTo(customer.getId());
        assertThat(dto.getTransactions()).hasSize(1);
        assertThat(dto.getTransactions().iterator().next()).usingRecursiveComparison().isEqualTo(transaction);
        assertThat(dto.getHistory()).hasSize(1);
        assertThat(dto.getHistory().iterator().next()).usingRecursiveComparison().isEqualTo(history);

        verify(transactionMapper).toDto(transactionEntity);
        verify(transactionMapper).toDto(historyEntity);
        verify(customerMapper).toDto(customerEntity);
        verifyNoMoreInteractions(customerMapper, transactionMapper);
    }

    @Test
    public void toDto_should_mapThrowNoSuchElementException_when_customerIsMissing() {
        Instant date = Instant.now();
        BankAccount dto = new CheckingBankAccount();
        dto.setId(10L);
        dto.setType(AccountType.CHECKING);
        dto.setBalance(new Money(new BigDecimal("1000")));
        Set<Long> customers = new HashSet<>();
        Customer customer = createCustomer();
        customers.add(customer.getId());
        dto.setCustomersId(customers);
        Set<Transaction> transactions = new HashSet<>();
        Transaction transaction = createTransaction(5000L, 10L, date);
        transactions.add(transaction);
        dto.setTransactions(transactions);
        Set<Transaction> histories = new HashSet<>();
        Transaction history = createTransaction(2000L, 10L, date);
        histories.add(history);
        dto.setHistory(histories);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            bankAccountMapper.toEntity(dto);
            fail();
        } catch (NoSuchElementException exception) {
            assertThat(exception.getMessage()).hasToString("Customer entity not found for id: 99");
        }

        verify(customerRepository).findById(customer.getId());
        verifyNoInteractions(customerMapper, transactionMapper);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    public void toDto_should_mapEntityValues_when_dtoHasValues() {
        Instant date = Instant.now();
        BankAccount dto = new CheckingBankAccount();
        dto.setId(10L);
        dto.setType(AccountType.CHECKING);
        dto.setBalance(new Money(new BigDecimal("1000")));
        Set<Long> customers = new HashSet<>();
        Customer customer = createCustomer();
        customers.add(customer.getId());
        dto.setCustomersId(customers);
        Set<Transaction> transactions = new HashSet<>();
        Transaction transaction = createTransaction(5000L, 10L, date);
        transactions.add(transaction);
        dto.setTransactions(transactions);
        Set<Transaction> histories = new HashSet<>();
        Transaction history = createTransaction(2000L, 10L, date);
        histories.add(history);
        dto.setHistory(histories);

        CustomerEntity customerEntity = createCustomerEntity();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerMapper.toEntity(customer)).thenReturn(customerEntity);
        TransactionEntity transactionEntity = createTransactionEntity(5000L, 10L, date);
        when(transactionMapper.toEntity(transaction)).thenReturn(transactionEntity);
        TransactionEntity historyEntity = createTransactionEntity(2000L, 10L, date);
        when(transactionMapper.toEntity(history)).thenReturn(historyEntity);
        BankAccountEntity entity = bankAccountMapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(10L);
        assertThat(entity.getType()).isEqualTo(AccountType.CHECKING);
        assertThat(entity.getBalance()).usingRecursiveComparison().isEqualTo(new BigDecimal("1000"));
        assertThat(entity.getCustomers()).hasSize(1);
        assertThat(entity.getCustomers().iterator().next()).isEqualTo(customerEntity);
        assertThat(entity.getTransactions()).hasSize(1);
        assertThat(entity.getTransactions().iterator().next()).usingRecursiveComparison().isEqualTo(transactionEntity);
        assertThat(entity.getHistory()).hasSize(1);
        assertThat(entity.getHistory().iterator().next()).usingRecursiveComparison().isEqualTo(historyEntity);

        verify(customerRepository).findById(customer.getId());
        verify(transactionMapper).toEntity(history);
        verify(transactionMapper).toEntity(transaction);
        verify(customerMapper).toEntity(customer);
        verifyNoMoreInteractions(customerMapper, transactionMapper, customerRepository);
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setId(99L);
        customer.setFirstName("Paul");
        customer.setLastName("Martin");
        customer.setGender(Gender.MALE);
        customer.setMaritalStatus(MaritalStatus.SINGLE);
        customer.setBirthdate(LocalDateTime.of(1995, Month.MAY, 3, 6, 30, 40, 50000));
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
        customerEntity.setBirthdate(LocalDateTime.of(1995, Month.MAY, 3, 6, 30, 40, 50000));
        customerEntity.setCountry("FR");
        customerEntity.setAddress("100 avenue de la république");
        customerEntity.setCity("Paris");
        customerEntity.setEmail("jean.dupont@yahoo.fr");
        customerEntity.setPhoneNumber("+33642645678");
        return customerEntity;
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
