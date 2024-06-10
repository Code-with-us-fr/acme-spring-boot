package com.acme.starters.keycloak.client;

import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ClientBearerSpringConfiguration {

    @Bean
    public ClientBearerHttpRequestInterceptor clientBearerHttpRequestInterceptor(Keycloak keycloak) {
        return new ClientBearerHttpRequestInterceptor(keycloak);
    }
}
