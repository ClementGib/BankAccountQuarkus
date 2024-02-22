package com.cdx.bas.client.bank.transaction;

import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionControllerPort;
import com.cdx.bas.domain.bank.transaction.TransactionException;
import com.cdx.bas.domain.bank.transaction.TransactionServicePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Set;

@Path("/transactions")
@ApplicationScoped
public class TransactionResource implements TransactionControllerPort {

    private static final Logger logger = LoggerFactory.getLogger(TransactionResource.class);

    @Inject
    TransactionServicePort transactionServicePort;

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Transaction> getAll() {
        return transactionServicePort.getAll();
    }

    @GET
    @Path("/{status}")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Transaction> getAllByStatus(@PathParam("status") String status) {
        try {
            return transactionServicePort.findAllByStatus(status);
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.warn("Error: " + illegalArgumentException.getCause());
            return Collections.emptySet();
        }
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add deposit transaction", description = "Returns acceptance information about the added transaction")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Deposit transaction accepted"),
            @APIResponse(responseCode = "400", description = "Transaction invalid check error details"),
            @APIResponse(responseCode = "500", description = "Unexpected error happened")
    })
    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Response deposit(@PathParam("id") Long id, Transaction depositTransaction) {
        try {
            transactionServicePort.createTransaction(depositTransaction);
            return Response.status(Response.Status.ACCEPTED).entity("Deposit transaction accepted").build();
        } catch (TransactionException transactionException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(transactionException.getMessage()).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unexpected error happened").build();
        }
    }
}
