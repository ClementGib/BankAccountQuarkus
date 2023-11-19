package com.cdx.bas.domain.validator;

import com.cdx.bas.domain.utils.ExchangeRateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
    }

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext context) {
        return ExchangeRateUtils.hasCurrency(currency);
    }
}