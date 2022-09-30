package com.cdx.bas.application.bank.account;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.cdx.bas.application.customer.CustomerEntity;
import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.application.transaction.TransactionEntity;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.customer.Customer;
import com.cdx.bas.domain.customer.CustomerPersistencePort;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.transaction.Transaction;

@RequestScoped
public class BankAccountMapper implements DtoEntityMapper<BankAccount, BankAccountEntity> {

	@Inject
	private DtoEntityMapper<Customer, CustomerEntity> customerMapper;

	@Inject
	private DtoEntityMapper<Transaction, TransactionEntity> transactionMapper;

	@Inject
	private CustomerPersistencePort customerRepository;

	@Override
	public BankAccount toDto(BankAccountEntity entity) {
		BankAccount dto = new BankAccount();
		return dto;
	}

	@Override
	public BankAccountEntity toEntity(BankAccount dto) {
		BankAccountEntity entity = new BankAccountEntity();
		return entity;
	}
}
