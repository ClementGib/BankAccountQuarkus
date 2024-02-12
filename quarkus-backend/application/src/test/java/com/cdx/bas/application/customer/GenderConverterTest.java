package com.cdx.bas.application.customer;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.cdx.bas.domain.customer.Gender.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class GenderConverterTest {
    private GenderConverter genderConverter;

    @BeforeEach
    public void setUp() {
        genderConverter = new GenderConverter();
    }
    @Test
    public void convertToDatabaseColumn_should_convertToGenderCode() {
        assertThat(genderConverter.convertToDatabaseColumn(MALE)).isEqualTo(strMale);
        assertThat(genderConverter.convertToDatabaseColumn(FEMALE)).isEqualTo(strFemale);
        assertThat(genderConverter.convertToDatabaseColumn(OTHER)).isEqualTo(strOther);
    }

    @Test
    public void convertToEntityAttribute_should_convertToGender() {
        assertThat(genderConverter.convertToEntityAttribute(strMale)).isEqualTo(MALE);
        assertThat(genderConverter.convertToEntityAttribute(strFemale)).isEqualTo(FEMALE);
        assertThat(genderConverter.convertToEntityAttribute(strOther)).isEqualTo(OTHER);
    }

    @Test
    public void convertToEntityAttribute_should_ThrowExceptionForInvalidDatabaseColumn() {
        assertThrows(IllegalStateException.class, () -> genderConverter.convertToEntityAttribute('X'));
    }

}