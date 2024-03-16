package com.cdx.bas.domain.bank.transaction.validation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidTypes {
    ValidType[] value();
}
