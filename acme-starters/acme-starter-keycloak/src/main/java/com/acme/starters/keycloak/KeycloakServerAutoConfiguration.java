package com.acme.starters.keycloak;

import com.acme.starters.keycloak.server.AuthenticationLogAttributesAppender;
import com.acme.starters.keycloak.server.KeycloakOAuth2ResourceServerConfigurerCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableWebSecurity
public class KeycloakServerAutoConfiguration {

    @Bean
    public KeycloakOAuth2ResourceServerConfigurerCustomizer keycloakJwtAuthenticationConverter(KeycloakProperties keycloakProperties) {
        return new KeycloakOAuth2ResourceServerConfigurerCustomizer(keycloakProperties);
    }

    @Bean
    public AuthenticationLogAttributesAppender authenticationLogAttributesAppender() {
        return new AuthenticationLogAttributesAppender();
    }
}
