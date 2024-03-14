package com.cdx.bas.domain.bank.transaction.status;

import com.cdx.bas.domain.bank.transaction.Transaction;

import java.util.Map;

public interface TransactionStatusServicePort {
    /**
     * Set transaction to outstanding with additional metadata and avoid multiple process
     *
     * @param transaction to set as OUTSTANDING
     * @return outstanding transaction
     */
    Transaction setAsOutstanding(Transaction transaction);

    /**
     * Complete processed transaction
     *
     * @param transaction to change status
     * @param status to set to the transaction
     * @param metadata with detail about the status
     */
    Transaction setStatus(Transaction transaction, TransactionStatus status, Map<String, String> metadata);

}
