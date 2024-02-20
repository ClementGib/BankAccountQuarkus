package com.cdx.bas.client.bank.account;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class BankAccountResourceTest {

    @Test
    public void deposite_shouldCreateDepositeTransaction_whenBankAccountFound_andDepositeTransactionIsValid() {

    }

    @Test
    public void deposite_shouldReturnHTTPError_whenBankAccountFound_butDepositeTransactionIsInvalid() {

    }

    @Test
    public void deposite_shouldReturnHTTPError_whenBankAccountFound_butDepositeAmountReach() {

    }

}