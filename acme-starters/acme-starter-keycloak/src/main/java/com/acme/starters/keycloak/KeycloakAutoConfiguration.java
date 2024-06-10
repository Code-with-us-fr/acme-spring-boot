package com.acme.starters.keycloak;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(KeycloakProperties.class)
@Import({KeycloakClientAutoConfiguration.class, KeycloakServerAutoConfiguration.class})
public class KeycloakAutoConfiguration {

}
