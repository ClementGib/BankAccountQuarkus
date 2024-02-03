package com.cdx.bas.client.customer;


import com.cdx.bas.application.customer.CustomerRepository;
import com.cdx.bas.domain.customer.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;

@ApplicationScoped
@Path("/customers")
public class CustomerResource {

    CustomerRepository customerRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Customer> getAll() {
        return customerRepository.getAll();
    }
}
