package com.cdx.bas.domain.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
@Documented
public @interface ValidCurrency {
    String message() default "currency should be in the exchange rate map.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
