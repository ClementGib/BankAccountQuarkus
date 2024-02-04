package com.cdx.bas.client.customer;


import com.cdx.bas.domain.customer.Customer;
import com.cdx.bas.domain.customer.CustomerPersistencePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;

import static jakarta.transaction.Transactional.TxType.*;

@ApplicationScoped
@Path("/customers")
public class CustomerResource {

    @Inject
    CustomerPersistencePort customerPersistencePort;

    @GET
    @Transactional(value = REQUIRED)
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Customer> getAll() {
        return customerPersistencePort.getAll();
    }
}
