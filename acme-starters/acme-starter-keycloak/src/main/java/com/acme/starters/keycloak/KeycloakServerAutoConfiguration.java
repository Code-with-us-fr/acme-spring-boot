package com.acme.starters.keycloak;

import com.acme.starters.keycloak.server.AuthenticationLogAttributesAppender;
import com.acme.starters.keycloak.server.KeycloakOAuth2ResourceServerConfigurerCustomizer;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableWebSecurity
public class KeycloakServerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "keycloakJwtAuthenticationConverter")
    public KeycloakOAuth2ResourceServerConfigurerCustomizer keycloakJwtAuthenticationConverter(KeycloakProperties keycloakProperties) {
        return new KeycloakOAuth2ResourceServerConfigurerCustomizer(keycloakProperties);
    }

    @Bean
    @ConditionalOnClass(EndpointRequest.class)
    @ConditionalOnMissingBean(name = "managementFilterChain")
    @Order(HIGHEST_PRECEDENCE + 100)
    public SecurityFilterChain managementFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    @ConditionalOnClass(SwaggerConfig.class)
    @ConditionalOnMissingBean(name = "swaggerFilterChain")
    @Order(HIGHEST_PRECEDENCE + 100)
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/v3/api-docs/**", "/swagger-ui/**")
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationLogAttributesAppender authenticationLogAttributesAppender() {
        return new AuthenticationLogAttributesAppender();
    }
}
