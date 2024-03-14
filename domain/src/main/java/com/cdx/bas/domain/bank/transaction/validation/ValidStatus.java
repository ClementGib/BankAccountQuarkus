package com.cdx.bas.domain.bank.transaction.validation;

import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusValidator.class)
@Documented
public @interface ValidStatus {

    TransactionStatus expectedStatus() default TransactionStatus.UNPROCESSED;
    String message() default "Unexpected transaction status.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
