package com.cdx.bas.domain.customer;

import java.util.Optional;
import java.util.Set;

public interface CustomerPersistencePort {

    /**
     * find every Customers
     * @return all Customer
     */
    public Set<Customer> getAll();

    
    /**
     * find Customer from its id
     * 
     * @param id of Customer
     * @return <Optional>Customer if id corresponding or not to a Customer
     */
    public Optional<Customer> findById(long id);
    
    /**
     * create the current Customer
     * 
     * @param Customer to create
     * @return created Customer
     */
    public Customer create(Customer customer);
    
    /**
     * update the current Customer
     * 
     * @param Customer to update
     * @return updated Customer
     */
    public Customer update(Customer customer);
    
    /**
     * delete Customer from its id
     * 
     * @param id of Customer
     * @return Customer if id corresponding or not to a Customer
     */
    public Optional<Customer> deleteById(long id);
}
