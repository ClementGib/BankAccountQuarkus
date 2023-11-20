package com.cdx.bas.application.customer;


import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.customer.Customer;
import com.cdx.bas.domain.customer.CustomerPersistencePort;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

/***
 * persistence implementation for Customer entities
 * 
 * @author Clément Gibert
 *
 */
@ApplicationScoped
public class CustomerRepository implements CustomerPersistencePort, PanacheRepositoryBase<CustomerEntity, Long>  {
	
    private static final Logger logger = Logger.getLogger(CustomerRepository.class);
    
    @Inject
    private DtoEntityMapper<Customer, CustomerEntity> customerMapper;

	@Override
	public Optional<Customer> findById(long id) {
		return findByIdOptional(id).map(customerMapper::toDto);
	}

	@Override
	public Customer create(Customer customer) {
        persist(customerMapper.toEntity(customer));
        logger.info("Customer " + customer.getId() + " created");
        return customer;
	}

	@Override
	public Customer update(Customer customer) {
	    persist(customerMapper.toEntity(customer));
        logger.info("Customer " + customer.getId() + " updated");
        return customer;
	}

	@Override
	public Optional<Customer> deleteById(long id) {
        Optional<CustomerEntity> entityOptional = findByIdOptional(id);
        if (entityOptional.isPresent()) {
            CustomerEntity entity = entityOptional.get();
            delete(entity);
            logger.info("Customer " + entity.getId() + " deleted");
            return Optional.of(customerMapper.toDto(entity));
        }
        return Optional.empty();
	}

}
