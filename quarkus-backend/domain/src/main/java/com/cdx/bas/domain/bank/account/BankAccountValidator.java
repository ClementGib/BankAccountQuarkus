package com.cdx.bas.domain.bank.account;

import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@RequestScoped
public class BankAccountValidator {

    @Inject
    Validator validator;
    
    public void validateBankAccount(BankAccount bankAccount) throws BankAccountException {
        Set<ConstraintViolation<BankAccount>> violations = validator.validate(bankAccount);
        if (!violations.isEmpty()) {
            throw new BankAccountException(concatViolations(violations));
        }
    }
    
    private static String concatViolations(Set<ConstraintViolation<BankAccount>> violations) {
        StringBuilder violationBuilder = new StringBuilder();
        for (ConstraintViolation<BankAccount> violation : violations) {
            violationBuilder.append(violation.getMessage());
            violationBuilder.append("\n");
        }
        return violationBuilder.toString();
    }
}
