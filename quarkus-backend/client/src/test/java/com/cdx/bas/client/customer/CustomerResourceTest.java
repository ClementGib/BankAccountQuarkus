package com.cdx.bas.client.customer;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class CustomerResourceTest {

//    @InjectMock
//    CustomerPersistencePort customerPersistencePort;
//
//    @Inject
//    CustomerResource customerResource;

    @Test
    public void test() {

    }

}