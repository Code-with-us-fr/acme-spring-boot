package com.acme.starters.keycloak;

import com.acme.starters.keycloak.client.ClientBearerSpringConfiguration;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "keycloak.grant-type")
@Import({ClientBearerSpringConfiguration.class})
public class KeycloakClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Keycloak keycloakClient(KeycloakProperties properties) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getAuthServerUrl())
                .realm(properties.getRealm())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .grantType(properties.getGrantType())
                .build();
    }

    @Bean
    public RealmResource mainRealmResource(Keycloak keycloak, KeycloakProperties keycloakProperties) {
        return keycloak.realm(keycloakProperties.getRealm());
    }
}
