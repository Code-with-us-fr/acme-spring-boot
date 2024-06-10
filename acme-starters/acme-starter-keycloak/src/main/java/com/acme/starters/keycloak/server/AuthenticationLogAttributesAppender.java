package com.acme.starters.keycloak.server;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.io.IOException;

/**
 * Append authentication logging attributes to facilitate diagnostics.
 */
public class AuthenticationLogAttributesAppender implements ApplicationListener<AuthenticationSuccessEvent>, Filter {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        if (event.getAuthentication() instanceof JwtAuthenticationToken jwtAuthenticationToken
                && jwtAuthenticationToken.getToken() != null
                && jwtAuthenticationToken.getToken().getIssuer() != null) {

            addAuthenticationLogAttributes(jwtAuthenticationToken.getToken());
        }
    }

    private static void addAuthenticationLogAttributes(Jwt token) {
        MDC.put("userId", token.getSubject());
        MDC.put("authIssuer", token.getIssuer().toString());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            cleanAuthenticationLogAttributes();
        }
    }

    private static void cleanAuthenticationLogAttributes() {
        MDC.remove("userId");
        MDC.remove("authIssuer");
    }
}
