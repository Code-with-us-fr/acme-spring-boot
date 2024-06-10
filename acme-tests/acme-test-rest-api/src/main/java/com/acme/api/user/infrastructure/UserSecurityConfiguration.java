package com.acme.api.user.infrastructure;

import com.acme.starters.keycloak.server.KeycloakOAuth2ResourceServerConfigurerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.POST;

@Configuration(proxyBeanMethods = false)
public class UserSecurityConfiguration {

    private final KeycloakOAuth2ResourceServerConfigurerCustomizer oauth2ResourceServerCustomizer;

    public UserSecurityConfiguration(KeycloakOAuth2ResourceServerConfigurerCustomizer oauth2ResourceServerCustomizer) {
        this.oauth2ResourceServerCustomizer = oauth2ResourceServerCustomizer;
    }

    @Bean
    SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(POST, "/users/**").hasRole("writer")
                .requestMatchers("/users/**").authenticated()
                .anyRequest().denyAll());

        http.oauth2ResourceServer(oauth2ResourceServerCustomizer);

        return http.build();
    }
}
