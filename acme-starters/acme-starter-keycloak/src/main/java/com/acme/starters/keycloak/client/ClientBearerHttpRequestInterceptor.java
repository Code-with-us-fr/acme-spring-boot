package com.acme.starters.keycloak.client;

import org.keycloak.admin.client.Keycloak;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * A {@link ClientHttpRequestInterceptor} that adds the Bearer Token from a {@link Keycloak} client.
 */
public class ClientBearerHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final Keycloak keycloak;

    public ClientBearerHttpRequestInterceptor(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().setBearerAuth(keycloak.tokenManager().getAccessTokenString());
        return execution.execute(request, body);
    }
}
