package com.cdx.bas.domain.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.cdx.bas.domain.bank.account.BankAccount;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CustomerTest {

	@Test
	public void Customer_should_returnValidCustomerObject_when_fillAllFieldsWithValidValues() {
		ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
		accounts.add(new BankAccount());
		HashMap<String, String> metadatas = new HashMap<String, String>();
		metadatas.put("contact_preferences", "email");
		metadatas.put("annual_salary", "48000");

		Customer customer = new Customer();
		customer.setId(10);
		customer.setFirstName("Jean");
		customer.setLastName("Dupont");
		customer.setGender(Gender.MALE);
		customer.setMaritalStatus(MaritalStatus.SINGLE);
		customer.setBirthdate(LocalDate.of(1995, 05, 05));
		customer.setNationality("FR");
		customer.setAddress("100 avenue de la république");
		customer.setCity("Paris");
		customer.setEmail("jean.dupont@yahoo.fr");
		customer.setPhoneNumber("+33642645678");
		customer.setAccounts(accounts);
		customer.setMetadatas(metadatas);
		
		customer.validate();

		assertThat(customer.getId()).isEqualTo(10);
		assertThat(customer.getFirstName()).hasToString("Jean");
		assertThat(customer.getLastName()).hasToString("Dupont");
		assertThat(customer.getGender()).isEqualTo(Gender.MALE);
		assertThat(customer.getBirthdate()).isBefore(LocalDate.now());
		assertThat(customer.getBirthdate().toString()).hasToString("1995-05-05");
		assertThat(customer.getNationality()).isEqualTo("FR");
		assertThat(customer.getMaritalStatus()).isEqualTo(MaritalStatus.SINGLE);
		assertThat(customer.getAddress()).hasToString("100 avenue de la république");
		assertThat(customer.getCity()).hasToString("Paris");
		assertThat(customer.getEmail()).hasToString("jean.dupont@yahoo.fr");
		assertThat(customer.getPhoneNumber()).hasToString("+33642645678");
		assertThat(customer.getAccounts().size()).isEqualTo(1);
		assertThat(customer.getMetadatas().size()).isEqualTo(2);
		assertThat(customer.getMetadatas().get("contact_preferences")).hasToString("email");
		assertThat(customer.getMetadatas().get("annual_salary")).hasToString("48000");
	}

	@Test
	public void Customer_should_returnValidCustomerObject_when_optionalFieldsAreMissing() {
		Customer customer = new Customer();
		customer.setId(10);
		customer.setFirstName("Jean");
		customer.setLastName("Dupont");
		customer.setGender(Gender.MALE);
		customer.setMaritalStatus(MaritalStatus.SINGLE);
		customer.setBirthdate(LocalDate.of(1995, 05, 05));
		customer.setNationality("FR");
		customer.setAddress("100 avenue de la république");
		customer.setCity("Paris");
		customer.setEmail("jean.dupont@yahoo.fr");
		customer.setPhoneNumber("+33642645678");
		customer.setAccounts(null);
		customer.setMetadatas(null);
		
		customer.validate();
		
		assertThat(customer.getId()).isEqualTo(10);
		assertThat(customer.getFirstName()).hasToString("Jean");
		assertThat(customer.getLastName()).hasToString("Dupont");
		assertThat(customer.getGender()).isEqualTo(Gender.MALE);
		assertThat(customer.getBirthdate()).isBefore(LocalDate.now());
		assertThat(customer.getBirthdate().toString()).hasToString("1995-05-05");
		assertThat(customer.getNationality()).isEqualTo("FR");
		assertThat(customer.getMaritalStatus()).isEqualTo(MaritalStatus.SINGLE);
		assertThat(customer.getAddress()).hasToString("100 avenue de la république");
		assertThat(customer.getCity()).hasToString("Paris");
		assertThat(customer.getEmail()).hasToString("jean.dupont@yahoo.fr");
		assertThat(customer.getPhoneNumber()).hasToString("+33642645678");
		assertThat(customer.getAccounts()).isNull();
		assertThat(customer.getMetadatas()).isNull();
	}
    
    @Test
    public void Customer_should_throwIllegalStateExceptionWithSpecificMessages_when_idIsLowerThanOne() {
        try {
        	Customer customer = new Customer(0L,
                    "Jean",
                    "Dupont",
                    Gender.MALE,
                    MaritalStatus.SINGLE,
                    LocalDate.of(1995, 05, 05),
                    "FR",
                    "100 avenue de la république",
                    "Paris",
                    "jean.dupont@yahoo.fr",
                    "+33642645678",
                    null,
                    null);
        	
        	customer.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("id must be positive and higher than 0.\n");
        }
    }
    
    @Test
    public void Customer_should_throwIllegalStateExceptionWithSpecificMessages_when_firstNameIsEmptyString() {
        try {
        	Customer customer = new Customer(10L,
                    "",
                    "Dupont",
                    Gender.MALE,
                    MaritalStatus.SINGLE,
                    LocalDate.of(1995, 05, 05),
                    "FR",
                    "100 avenue de la république",
                    "Paris",
                    "jean.dupont@yahoo.fr",
                    "+33642645678",
                    null,
                    null);

            customer.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("firstName must contain at least 1 character.\n");
        }
    }

    @Test
    public void Customer_should_throwIllegalStateExceptionWithSpecificMessages_when_firstNameIsTooLong() {
        try {
            String longStr = "Blaine Charles David Earl Frederick Gerald Hubert Irvin John Kenneth Lloyd Martin "
                    + "Nero Oliver Paul Quincy Randolph Sherman Thomas Uncas Victor William Xerxes Yancy Zeus "
                    + "Wolfeschlegelsteinhausenbergerdorffwelchevoralternwarengewissenhaftschaferswessenschaf"
                    + "ewarenwohlgepflegeundsorgfaltigkeitbeschutzenvonangreifendurchihrraubgierigfeindewelch"
                    + "evoralternzwolftausendjahresvorandieerscheinenvanderersteerdemenschderraumschiffgebrau"
                    + "chlichtalsseinursprungvonkraftgestartseinlangefahrthinzwischensternartigraumaufdersuch"
                    + "enachdiesternwelchegehabtbewohnbarplanetenkreisedrehensichundwohinderneurassevonversta"
                    + "ndigmenschlichkeitkonntefortpflanzenundsicherfreuenanlebenslanglichfreudeundruhemitnic"
                    + "hteinfurchtvorangreifenvonandererintelligentgeschopfsvonhinzwischensternartigraum.";

            Customer customer = new Customer(10L,
                    longStr,
                    "Dupont",
                    Gender.MALE,
                    MaritalStatus.SINGLE,
                    LocalDate.of(1995, 05, 05),
                    "FR",
                    "100 avenue de la république",
                    "Paris",
                    "jean.dupont@yahoo.fr",
                    "+33642645678",
                    null,
                    null);
            
        	customer.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("firstName cannot have more than 750 characters.\n");
        }
    }

    @Test
    public void Customer_should_throwIllegalStateExceptionWithSpecificMessages_when_lastNameIsEmptyString() {
        try {
        	Customer customer = new Customer(10L,
                    "Jean",
                    "",
                    Gender.MALE,
                    MaritalStatus.SINGLE,
                    LocalDate.of(1995, 05, 05),
                    "FR",
                    "100 avenue de la république", 
                    "Paris", 
                   "jean.dupont@yahoo.fr",
                   "+33642645678",
                   null,
                   null);

        	customer.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("lastName  must contain at least 1 character.\n");
        }
    }

    @Test
    public void Customer_should_throwIllegalStateExceptionWithSpecificMessages_when_lastNameIsTooLong() {
        try {
            String longStr = "Blaine Charles David Earl Frederick Gerald Hubert Irvin John Kenneth Lloyd Martin "
                    + "Nero Oliver Paul Quincy Randolph Sherman Thomas Uncas Victor William Xerxes Yancy Zeus "
                    + "Wolfeschlegelsteinhausenbergerdorffwelchevoralternwarengewissenhaftschaferswessenschaf"
                    + "ewarenwohlgepflegeundsorgfaltigkeitbeschutzenvonangreifendurchihrraubgierigfeindewelch"
                    + "evoralternzwolftausendjahresvorandieerscheinenvanderersteerdemenschderraumschiffgebrau"
                    + "chlichtalsseinursprungvonkraftgestartseinlangefahrthinzwischensternartigraumaufdersuch"
                    + "enachdiesternwelchegehabtbewohnbarplanetenkreisedrehensichundwohinderneurassevonversta"
                    + "ndigmenschlichkeitkonntefortpflanzenundsicherfreuenanlebenslanglichfreudeundruhemitnic"
                    + "hteinfurchtvorangreifenvonandererintelligentgeschopfsvonhinzwischensternartigraum.";

            Customer customer = new Customer(10L,
                    "Jean",
                    longStr,
                    Gender.MALE,
                    MaritalStatus.SINGLE,
                    LocalDate.of(1995, 05, 05),
                    "FR",
                    "100 avenue de la république",
                    "Paris",
                   "jean.dupont@yahoo.fr",
                   "+33642645678",
                   null,
                   null);

        	customer.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("lastName cannot have more than 750 characters.\n");
        }
    }
    
    @Test
    public void Customer_should_throwIllegalStateExceptionWithSpecificMessages_when_birthdateIsAfterCurrentDate() {
        try {
        	Customer customer = new Customer(10L,
                    "Jean",
                    "Dupont",
                    Gender.MALE,
                    MaritalStatus.SINGLE,
                    LocalDate.of(2099, 05, 05),
                    "FR",
                    "100 avenue de la république",
                    "Paris",
                    "jean.dupont@yahoo.fr",
                    "+33642645678",
                    null,
                    null);

        	customer.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("birthdate cannot be null and before the current time.\n");
        }
    }
    
    @Test
    public void Customer_should_throwIllegalStateExceptionWithSpecificMessages_when_genderOrMaritalStatusAreNull() {
        try {
        	Customer customer = new Customer(10L,
                    "Jean",
                    "Dupont",
                    null,
                    null,
                    LocalDate.of(1995, 05, 05),
                    "FR",
                    "100 avenue de la république",
                    "Paris",
                    "jean.dupont@yahoo.fr",
                    "+33642645678",
                    null,
                    null);

        	customer.validate();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception.getMessage()).hasToString("gender must not be null.\nmaritalStatus must not be null.\n");
        }
    }
}
