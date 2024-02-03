package com.cdx.bas.client.customer;

import com.cdx.bas.application.customer.CustomerRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class CustomerResourceTest {

    @InjectMock
    CustomerRepository customerRepository;

    @Inject
    CustomerResource customerResource;

    @Test
    public void test() {

    }

}