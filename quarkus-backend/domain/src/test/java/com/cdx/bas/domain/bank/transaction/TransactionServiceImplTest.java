package com.cdx.bas.domain.bank.transaction;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TransactionServiceImplTest {

    
    @Test
    public void processTransaction_should_processBankAccountDeposit_when_creditTransactionWithPositiveAmount() {
        
    }
    
    @Test
    public void processTransaction_should_throwTransactionException_when_creditTransactionWithNegativeAmount() {
        
    }
}
