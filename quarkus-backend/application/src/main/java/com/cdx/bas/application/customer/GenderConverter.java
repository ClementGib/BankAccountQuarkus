package com.cdx.bas.application.customer;

import com.cdx.bas.domain.customer.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static com.cdx.bas.domain.customer.Gender.*;

@Converter
public class GenderConverter implements AttributeConverter<Gender, Character> {

    @Override
    public Character convertToDatabaseColumn(Gender gender) {
        return switch (gender) {
            case MALE -> strMale;
            case FEMALE -> strFemale;
            case OTHER -> strOther;
        };
    }

    @Override
    public Gender convertToEntityAttribute(Character genderCode) {
        return switch (genderCode) {
            case strMale -> MALE;
            case strFemale -> FEMALE;
            case strOther -> OTHER;
            default -> throw new IllegalStateException("Unexpected gender value: " + genderCode);
        };
    }
}
