package com.acme.api.common.interfaces;

import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestingJwts {

    public static final String ADMIN_TOKEN = "ADMIN";
    public static final String READER_TOKEN = "READER";


    public static Jwt fromToken(String token) {
        Instant issuedDate = Instant.now();
        return switch (token) {
            case ADMIN_TOKEN -> new Jwt(
                    ADMIN_TOKEN,
                    issuedDate,
                    issuedDate.plusSeconds(120),
                    Map.of("alg", "none"),
                    Map.of(
                            "sub", UUID.randomUUID().toString(),
                            "realm_access", Map.of(
                                    "roles", List.of("offline_access")),
                            "resource_access", Map.of(
                                    "user-api", Map.of(
                                            "roles", List.of("reader", "writer")))));
            case READER_TOKEN -> new Jwt(
                    ADMIN_TOKEN,
                    issuedDate,
                    issuedDate.plusSeconds(120),
                    Map.of("alg", "none"),
                    Map.of(
                            "sub", UUID.randomUUID().toString(),
                            "realm_access", Map.of(
                                    "roles", List.of("offline_access")),
                            "resource_access", Map.of(
                                    "user-api", Map.of(
                                            "roles", List.of("reader")))));
            default -> throw new BadJwtException("invalid token");
        };
    }
}
