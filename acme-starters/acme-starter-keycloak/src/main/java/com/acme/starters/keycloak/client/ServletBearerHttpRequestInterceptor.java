package com.acme.starters.keycloak.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * A {@link ClientHttpRequestInterceptor} that adds the Bearer Token from an existing {@link org.springframework.security.oauth2.core.OAuth2AccessToken} tied to the current {@link Authentication}.
 * Suitable for Servlet applications, applying it to a typical {@link RestTemplate} configuration.
 */
public class ServletBearerHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return execution.execute(request, body);
        }

        if (!(authentication.getCredentials() instanceof AbstractOAuth2Token token)) {
            return execution.execute(request, body);
        }

        request.getHeaders().setBearerAuth(token.getTokenValue());
        return execution.execute(request, body);
    }
}
