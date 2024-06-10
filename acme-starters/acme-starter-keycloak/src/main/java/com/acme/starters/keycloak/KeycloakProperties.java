package com.acme.starters.keycloak;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("keycloak")
public class KeycloakProperties {

    /**
     * The URL of the Keycloak authentication server. (e.g. https://iam.acme.com)
     */
    private String authServerUrl;
    /**
     * The Keycloak realm.
     */
    private String realm = "acme";
    /**
     * The application client identifier.
     */
    private String clientId;
    /**
     * The application client secret.
     */
    private String clientSecret;
    /**
     * The client grant type (e.g. client_credential).
     */
    private String grantType;

    public String getAuthServerUrl() {
        return authServerUrl;
    }

    public void setAuthServerUrl(String authServerUrl) {
        this.authServerUrl = authServerUrl;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
