package com.cdx.bas.application.customer;


import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.customer.Customer;
import com.cdx.bas.domain.customer.CustomerPersistencePort;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

/***
 * specific dao interface for customers entities
 * 
 * @author Clément Gibert
 *
 */
@ApplicationScoped
public class CustomerRepository implements CustomerPersistencePort, PanacheRepository<CustomerEntity>  {
	
    private static final Logger logger = Logger.getLogger(CustomerRepository.class);
    
    @Inject
    private DtoEntityMapper<Customer, CustomerEntity> customerMapper;

	@Override
	public Optional<Customer> findById(long id) {
		return findByIdOptional(id).map(customerMapper::toDto);
	}

	@Override
	public Customer create(Customer customer) {
		return null;
	}

	@Override
	public Customer update(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Customer> deleteById(long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

}
