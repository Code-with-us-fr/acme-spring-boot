package com.acme.starters.keycloak.server;

import com.acme.starters.keycloak.KeycloakProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Convert a Keycloak {@link Jwt} to a {@link Collection} of {@link GrantedAuthority}.
 * Extract roles from {@code resource_access} claim or {@code realm_access} claim based on {@link KeycloakProperties}.
 */
public class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String RESOURCE_AUTHORITY_PREFIX = "ROLE_";
    private static final String REALM_AUTHORITY_PREFIX = "ROLE_realm_";
    public static final String RESOURCE_ACCESS_CLAIM = "resource_access";
    public static final String REALM_ACCESS_CLAIM = "realm_access";
    public static final String ROLES_SUB_CLAIM = "roles";

    private final KeycloakProperties keycloakProperties;

    public KeycloakJwtGrantedAuthoritiesConverter(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return Stream.concat(
                convertResourceRoleMappings(jwt).stream(),
                convertRealmRoleMappings(jwt).stream()
        ).toList();
    }

    private List<GrantedAuthority> convertResourceRoleMappings(Jwt jwt) {
        String clientId = keycloakProperties.getClientId();
        if (jwt.hasClaim(RESOURCE_ACCESS_CLAIM) && jwt.getClaim(RESOURCE_ACCESS_CLAIM) instanceof Map<?, ?> resourceAccess
                && resourceAccess.containsKey(clientId) && resourceAccess.get(clientId) instanceof Map<?, ?> resourceAccessClient
                && resourceAccessClient.containsKey(ROLES_SUB_CLAIM) && resourceAccessClient.get(ROLES_SUB_CLAIM) instanceof List<?> resourceAccessClientRoles) {
            return convertToGrantedAuthorities(resourceAccessClientRoles, RESOURCE_AUTHORITY_PREFIX);
        } else {
            return List.of();
        }
    }

    private List<GrantedAuthority> convertRealmRoleMappings(Jwt jwt) {
        if (jwt.hasClaim(REALM_ACCESS_CLAIM) && jwt.getClaim(REALM_ACCESS_CLAIM) instanceof Map<?,?> realmAccess
                && realmAccess.containsKey(ROLES_SUB_CLAIM) && realmAccess.get(ROLES_SUB_CLAIM) instanceof List<?> realmAccessRoles) {
            return convertToGrantedAuthorities(realmAccessRoles, REALM_AUTHORITY_PREFIX);
        } else {
            return List.of();
        }
    }

    private List<GrantedAuthority> convertToGrantedAuthorities(List<?> realmAccessRoles, String authorityPrefix) {
        return realmAccessRoles.stream()
                .map(role -> new SimpleGrantedAuthority(authorityPrefix + role.toString()))
                .collect(Collectors.toList());
    }
}
