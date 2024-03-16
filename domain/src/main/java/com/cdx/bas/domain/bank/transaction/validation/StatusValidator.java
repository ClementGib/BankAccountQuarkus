package com.cdx.bas.domain.bank.transaction.validation;

import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<ValidStatus, TransactionStatus> {

    private TransactionStatus expectedStatus;
    @Override
    public void initialize(ValidStatus constraintAnnotation) {
        this.expectedStatus = constraintAnnotation.expectedStatus();
    }

    @Override
    public boolean isValid(TransactionStatus value, ConstraintValidatorContext context) {
        return value != null && value == expectedStatus;
    }
}