package com.cdx.bas.domain.bank.transaction.validation;

import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.HashSet;
import java.util.Set;


@RequestScoped
public class TransactionValidator {

    @Inject
    Validator validator;

    public void validateNewDigitalTransaction(Transaction transaction) throws TransactionException {
        validateTransaction(transaction, NewTransactionGroup.class, DigitalTransactionGroup.class);
    }

    public void validateExistingDigitalTransaction(Transaction transaction) throws TransactionException {
        validateTransaction(transaction, ExistingTransactionGroup.class, DigitalTransactionGroup.class);
    }


    public void validateCashTransaction(Transaction transaction) throws TransactionException {
        validateTransaction(transaction, NewTransactionGroup.class, CashTransactionGroup.class);
    }

    private void validateTransaction(Transaction transaction, Class<?> stateGroup, Class<?> typeGroup) throws TransactionException {
        Set<ConstraintViolation<Transaction>> violations = new HashSet<>(validator.validate(transaction));
        violations.addAll(validator.validate(transaction, stateGroup));
        violations.addAll(validator.validate(transaction, typeGroup));
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