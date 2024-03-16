package com.cdx.bas.domain.bank.transaction.validation;

import com.cdx.bas.domain.bank.transaction.type.TransactionType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TypeValidator.class)
public @interface ValidType {

    TransactionType[] expectedTypes();
    String message() default "Transaction type is invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
