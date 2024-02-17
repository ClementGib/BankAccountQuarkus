package com.cdx.bas.client.transaction;

import com.cdx.bas.domain.transaction.Transaction;
import com.cdx.bas.domain.transaction.TransactionControllerPort;
import com.cdx.bas.domain.transaction.TransactionPersistencePort;
import com.cdx.bas.domain.transaction.TransactionStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

@Path("/transactions")
@ApplicationScoped
public class TransactionResource implements TransactionControllerPort {

    private static final Logger logger = LoggerFactory.getLogger(TransactionResource.class);

    @Inject
    TransactionPersistencePort transactionPersistencePort;

    @GET
    public Set<Transaction> getAll() {
        return transactionPersistencePort.getAll();
    }

    @GET
    @Path("/{status}")
    public Set<Transaction> getAllByStatus(@PathParam("status") String status) {
        try {
            TransactionStatus transactionStatus = TransactionStatus.fromString(status);
            return transactionPersistencePort.findAllByStatus(transactionStatus);
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.warn("Error: " + illegalArgumentException.getCause());
            return Collections.emptySet();
        }
    }
}
