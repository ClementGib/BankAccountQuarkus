package com.cdx.bas.domain.transaction.validation;

import com.cdx.bas.domain.transaction.TransactionStatus;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusValidator.class)
@Documented
public @interface ValidStatus {

    TransactionStatus expectedStatus() default TransactionStatus.UNPROCESSED;
    String message() default "unexpected transaction status.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
