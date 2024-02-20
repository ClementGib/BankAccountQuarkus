package com.cdx.bas.domain.currency;

import com.cdx.bas.domain.utils.ExchangeRateUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
    }

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext context) {
        return ExchangeRateUtils.hasCurrency(currency);
    }
}
