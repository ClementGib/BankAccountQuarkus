package com.cdx.bas.application.customer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.customer.Gender;
import com.cdx.bas.domain.customer.MaritalStatus;

@Entity
@Table(schema = "basapp", name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "customer_id"))
public class CustomerEntity {
    
    @Id
    @Column(name = "comment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_customer_id_seq_gen")
    @SequenceGenerator(name = "customers_customer_id_seq_gen", sequenceName = "customers_customer_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "gender", nullable = false)
    private Gender gender;
    
    @Column(name = "marital_status", nullable = false)
    private MaritalStatus maritalStatus;
    
    @Column(name = "birthday", nullable = false)
    private LocalDate birthdate;
    
    @Column(name = "nationality", nullable = false)
    private String nationality;
    
    @Column(name = "address", nullable = false)
    private String address;
    
    @Column(name = "city", nullable = false)
    private String city;
    
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "bank_accounts_customers", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private List<BankAccount> accounts;
    
    @Column(name = "metadatas", nullable = true)
    private Map<String, String> metadatas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
