package com.cdx.bas.application.bank.transaction.type;

import com.cdx.bas.application.bank.account.BankAccountServiceImpl;
import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.BankAccountException;
import com.cdx.bas.domain.bank.account.BankAccountServicePort;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionException;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatusServicePort;
import com.cdx.bas.domain.bank.transaction.type.TransactionTypeProcessingServicePort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.cdx.bas.domain.bank.transaction.status.TransactionStatus.*;
import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

@RequestScoped
public class TransactionTypeProcessingServiceImpl implements TransactionTypeProcessingServicePort {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    @Inject
    TransactionStatusServicePort transactionStatusService;

    @Inject
    BankAccountServicePort bankAccountService;


    @Override
    @Transactional(value = REQUIRES_NEW)
    public Transaction credit(Transaction transaction) {
        logger.info("Transaction " +  transaction.getId() + " processing...");

        try {
            BankAccount emitterBankAccount = bankAccountService.findBankAccount(transaction.getEmitterAccountId());
            BankAccount receiverBankAccount = bankAccountService.findBankAccount(transaction.getReceiverAccountId());
            Transaction currentTransaction = transactionStatusService.setAsOutstanding(transaction);

            logger.info("Process credit transaction " + currentTransaction.getId()
                    + " from bank account " + currentTransaction.getEmitterAccountId()
                    + " with amount " + currentTransaction.getAmount()
                    + " to bank account " + currentTransaction.getReceiverAccountId());

            Map<String, String> metadata = new HashMap<>();
            metadata.put("emitter_amount_before", emitterBankAccount.getBalance().getAmount().toString());
            metadata.put("receiver_amount_before", receiverBankAccount.getBalance().getAmount().toString());
            bankAccountService.transferAmountBetweenAccounts(currentTransaction, emitterBankAccount, receiverBankAccount);
            metadata.put("emitter_amount_after", emitterBankAccount.getBalance().getAmount().toString());
            metadata.put("receiver_amount_after", receiverBankAccount.getBalance().getAmount().toString());

            Transaction completedTransaction = transactionStatusService.setStatus(currentTransaction, COMPLETED, metadata);
            BankAccount updatedEmitterBankAccount = bankAccountService.addTransaction(completedTransaction, emitterBankAccount);
            bankAccountService.updateBankAccount(updatedEmitterBankAccount);
            bankAccountService.updateBankAccount(receiverBankAccount);
            logger.info("Bank account " + updatedEmitterBankAccount.getId() + " deposit transaction " + + currentTransaction.getId() + " completed.");
            return completedTransaction;
        } catch (NoSuchElementException exception) {
            logger.error("Transaction " + transaction.getId() + " deposit error for amount "+ transaction.getAmount() + ": " + exception.getMessage());
            Map<String, String> metadata = Map.of("error", exception.getMessage());
            return transactionStatusService.setStatus(transaction, ERROR, metadata);
        } catch (TransactionException exception) {
            logger.error("Transaction " + transaction.getId() + " of " + transaction.getAmount() + " is invalid.");
            Map<String, String> metadata = Map.of("error", exception.getMessage());
            return transactionStatusService.setStatus(transaction, REFUSED, metadata);
        } catch (BankAccountException exception) {
            logger.error("Transaction " + transaction.getId() + " deposit refused for amount "+ transaction.getAmount() + ": " + exception.getMessage());
            Map<String, String> metadata = Map.of("error", exception.getMessage());
            return transactionStatusService.setStatus(transaction, REFUSED, metadata);
        }
    }

    @Override
    public Transaction debit(Transaction transaction) {
        //TODO
        return null;
    }

    @Override
    public Transaction deposit(Transaction transaction) {
        //TODO
        return null;
    }

    @Override
    public Transaction withdraw(Transaction transaction) {
        //TODO
        return null;
    }
}
