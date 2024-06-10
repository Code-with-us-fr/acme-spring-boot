package com.acme.api.observability;

import com.acme.api.IntegrationTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.http.HttpStatus;

import static io.restassured.http.ContentType.TEXT;

@IntegrationTest
class ActuatorIT {

    @LocalManagementPort
    private int managementPort;

    @BeforeAll
    static void beforeAll() {
        RestAssured.replaceFiltersWith(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = managementPort;
    }

    @Test
    void shouldExposeLivenessProbe() {
        RestAssured.when().get("/actuator/health/liveness")
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldExposeReadinessProbe() {
        RestAssured.when().get("/actuator/health/readiness")
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldExposePrometheusEndpoint() {
        RestAssured.when().get("/actuator/prometheus")
                .then().statusCode(HttpStatus.OK.value())
                .contentType(TEXT);
    }
}
