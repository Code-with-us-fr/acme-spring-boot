package com.acme.api.user.interfaces;

import com.acme.api.user.domain.User;

public record UserRepresentation(
        String firstName,
        String lastName,
        String email
) {

    public static UserRepresentation fromDomain(User user) {
        return new UserRepresentation(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail());
    }
}
