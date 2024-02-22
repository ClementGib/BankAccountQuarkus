package com.cdx.bas.domain.bank.transaction.validation;

import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.HashSet;
import java.util.Set;

import static com.cdx.bas.domain.bank.transaction.type.TransactionType.DEPOSIT;


@RequestScoped
public class TransactionValidator {

    @Inject
    Validator validator;

    public void validateNewTransaction(Transaction transaction) throws TransactionException {
        validateTransaction(transaction, NewTransaction.class);
    }

    public void validateExistingTransaction(Transaction transaction) throws TransactionException {
        validateTransaction(transaction, ExistingTransaction.class);
    }

    private void validateTransaction(Transaction transaction, Class<?> group) throws TransactionException {
        Set<ConstraintViolation<Transaction>> violations = new HashSet<>(validator.validate(transaction));
        if (DEPOSIT.equals(transaction.getType())) {
            violations.addAll(validator.validate(transaction, CashMovement.class, group));
        } else {
            violations.addAll(validator.validate(transaction, AccountMovement.class, group));
        }
        checkConstraintViolation(violations);
    }

    private static void checkConstraintViolation(Set<ConstraintViolation<Transaction>> violations) {
        if (!violations.isEmpty()) {
            throw new TransactionException(concatViolations(violations));
        }
    }

    private static String concatViolations(Set<ConstraintViolation<Transaction>> violations) {
        StringBuilder violationBuilder = new StringBuilder();
        for (ConstraintViolation<Transaction> violation : violations) {
            violationBuilder.append(violation.getMessage());
            violationBuilder.append("\n");
        }
        return violationBuilder.toString();
    }
}