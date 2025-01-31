package com.aueb.team04.ft.domain;

import jakarta.persistence.*;

@Embeddable
public class Address {

    @Column(name="zipcode")
    private String zipcode;

    @Column(name="street_name")
    private String streetName;

    @Column(name="street_number")
    private String streetNumber;

    public Address() { }

    public Address(String zipcode, String streetName, String streetNumber) {
        this.zipcode = zipcode;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
    }

    // Getters and Setters
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
}