package com.cdx.bas.application.customer;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.cdx.bas.domain.customer.MaritalStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class MaritalStatusConverterTest {

    MaritalStatusConverter maritalStatusConverter;

    @BeforeEach
    public void setUp() {
        maritalStatusConverter = new MaritalStatusConverter();
    }
    @Test
    public void convertToDatabaseColumn_should_convertToMaritalCode() {
        assertEquals(strSingle, maritalStatusConverter.convertToDatabaseColumn(SINGLE));
        assertEquals(strMarried, maritalStatusConverter.convertToDatabaseColumn(MARRIED));
        assertEquals(strWidowed, maritalStatusConverter.convertToDatabaseColumn(WIDOWED));
        assertEquals(strDivorced, maritalStatusConverter.convertToDatabaseColumn(DIVORCED));
        assertEquals(strPacs, maritalStatusConverter.convertToDatabaseColumn(PACS));
    }

    @Test
    public void convertToEntityAttribute_should_convertToMaritalStatus() {
        assertEquals(SINGLE, maritalStatusConverter.convertToEntityAttribute(strSingle));
        assertEquals(MARRIED, maritalStatusConverter.convertToEntityAttribute(strMarried));
        assertEquals(WIDOWED, maritalStatusConverter.convertToEntityAttribute(strWidowed));
        assertEquals(DIVORCED, maritalStatusConverter.convertToEntityAttribute(strDivorced));
        assertEquals(PACS, maritalStatusConverter.convertToEntityAttribute(strPacs));
    }

    @Test
    public void convertToEntityAttribute_should_ThrowExceptionForInvalidDatabaseColumn() {
        assertThrows(IllegalStateException.class, () -> maritalStatusConverter.convertToEntityAttribute('X'));
    }
}