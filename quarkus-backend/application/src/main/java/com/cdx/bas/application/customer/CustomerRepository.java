package com.cdx.bas.application.customer;


import com.cdx.bas.application.mapper.DtoEntityMapper;
import com.cdx.bas.domain.customer.Customer;
import com.cdx.bas.domain.customer.CustomerPersistencePort;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.transaction.Transactional.TxType.MANDATORY;

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
    @Transactional(value = MANDATORY)
    public Set<Customer> getAll() {
        Set<Customer> all = findAll().stream()
                .map(customer -> customerMapper.toDto(customer))
                .collect(Collectors.toSet());
        return all;
    }

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
