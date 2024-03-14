package com.cdx.bas.domain.money;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

@NotNull
@Documented
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
@Constraint(validatedBy = AmountValidator.class)
public @interface Amount {
    String message() default "balance amount must respect the bank account restrictions.";
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
    
    long max() default Long.MAX_VALUE;

    long min() default Long.MIN_VALUE;
}
