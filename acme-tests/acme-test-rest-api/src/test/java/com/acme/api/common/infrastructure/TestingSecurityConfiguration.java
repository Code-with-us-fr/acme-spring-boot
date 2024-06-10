package com.acme.api.common.infrastructure;

import com.acme.api.common.interfaces.TestingJwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration(proxyBeanMethods = false)
public class TestingSecurityConfiguration {

    @Bean
    public JwtDecoder jwtDecoder() {
        return TestingJwts::fromToken;
    }
}
