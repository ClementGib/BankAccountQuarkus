package com.cdx.bas.client.customer;


import com.cdx.bas.application.customer.CustomerRepository;
import com.cdx.bas.domain.customer.Customer;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.Set;

@Path("/customers")
public class CustomerResource {

    CustomerRepository customerRepository;

    @GET
    public Set<Customer> getAll() {
        return customerRepository.getAll();
    }
}
