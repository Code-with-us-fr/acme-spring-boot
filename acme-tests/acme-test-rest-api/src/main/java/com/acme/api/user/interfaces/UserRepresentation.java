package com.acme.api.user.interfaces;

import com.acme.api.user.domain.Address;
import com.acme.api.user.domain.User;

import java.time.Instant;

import static java.util.Optional.ofNullable;

public record UserRepresentation(
        String firstName,
        String lastName,
        String email,
        String postalCode,
        String city
) {

    public User toDomain() {
        return new User()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setAddress(new Address()
                        .setPostalCode(postalCode)
                        .setCity(city)
                        .setUpdatedAt(Instant.now()));
    }

    public static UserRepresentation fromDomain(User user) {
        return new UserRepresentation(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                ofNullable(user.getAddress()).map(Address::getPostalCode).orElse(null),
                ofNullable(user.getAddress()).map(Address::getCity).orElse(null));
    }
}
