package com.acme.api.user.domain;

import java.time.Instant;

public class Address {
    private String postalCode;
    private String city;
    private Instant updatedAt;

    public String getPostalCode() {
        return postalCode;
    }

    public Address setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Address setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
