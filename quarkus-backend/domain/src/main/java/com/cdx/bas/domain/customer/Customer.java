package com.cdx.bas.domain.customer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.cdx.bas.domain.bank.account.BankAccount;

import net.dv8tion.jda.api.MessageBuilder;

/***
 * Banking service customer class
 * 
 */
public class Customer { // extends AbstractSelfValidator<Customer> {

	private Long id;
	
    private String firstName;
	
	private String lastName;
	
	private Gender gender;
	
	private MaritalStatus maritalStatus;
    
	private LocalDate birthdate;
    
	private String nationality;
    
	private String address;
	
	private String city;
    
	private String email;
	
	private String phoneNumber;
	
	private List<BankAccount> accounts;
	
	private Map<String, String> metadatas;
	
	public Customer() {
		super();
	}

	public Customer(Long id, String firstName, String lastName, Gender gender, MaritalStatus maritalStatus,
			LocalDate birthdate, String nationality, String address, String city, String email, String phoneNumber,
			List<BankAccount> accounts, Map<String, String> metadatas) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.maritalStatus = maritalStatus;
		this.birthdate = birthdate;
		this.nationality = nationality;
		this.address = address;
		this.city = city;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.accounts = accounts;
		this.metadatas = metadatas;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Gender getGender() {
		return gender;
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}
	
	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	
	public LocalDate getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getNationality() {
		return nationality;
	}
	
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public List<BankAccount> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(List<BankAccount> accounts) {
		this.accounts = accounts;
	}
	
	public Map<String, String> getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(Map<String, String> metadatas) {
		this.metadatas = metadatas;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(accounts, other.accounts) 
				&& Objects.equals(address, other.address)
				&& Objects.equals(birthdate, other.birthdate) 
				&& Objects.equals(city, other.city)
				&& Objects.equals(email, other.email) 
				&& Objects.equals(firstName, other.firstName)
				&& gender == other.gender 
				&& Objects.equals(lastName, other.lastName)
				&& maritalStatus == other.maritalStatus 
				&& Objects.equals(nationality, other.nationality)
				&& Objects.equals(phoneNumber, other.phoneNumber);
	}
	
	public void validate() {
        MessageBuilder messageBuilder = new MessageBuilder();
        
        if (messageBuilder.length() > 0) {
            throw new IllegalStateException(messageBuilder.build().getContentRaw());
        }
	}
}
