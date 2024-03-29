package com.cdx.bas.application.bank.transaction.status;

import com.cdx.bas.application.bank.transaction.TransactionRepository;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionException;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatusServicePort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Map;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.OUTSTANDING;
import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.UNPROCESSED;
import static jakarta.transaction.Transactional.TxType.MANDATORY;

@RequestScoped
public class TransactionStatusServiceImpl implements TransactionStatusServicePort {

    @Inject
    TransactionRepository transactionPersistencePort;

    @Override
    @Transactional(value = MANDATORY)
    public Transaction setAsOutstanding(Transaction transaction) throws TransactionException {
        if (UNPROCESSED.equals(transaction.getStatus())) {
            transaction.setStatus(OUTSTANDING);
        } else {
            throw new TransactionException("Transaction is not longer unprocessed.");
        }
        return transactionPersistencePort.update(transaction);
    }

    @Override
    @Transactional(value = MANDATORY)
    public Transaction setStatus(Transaction transaction, TransactionStatus status, Map<String, String> metadata) throws TransactionException {
        if (transaction == null) {
            throw new TransactionException("Transaction is null.");
        }
        transaction.setStatus(status);
        transaction.setMetadata(metadata);
        return transactionPersistencePort.update(transaction);
    }
}
