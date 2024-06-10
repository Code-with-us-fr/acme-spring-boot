package com.acme.api.observability;

import com.acme.api.IntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.http.ContentType.HTML;
import static io.restassured.http.ContentType.JSON;

@IntegrationTest
class SwaggerIT {

    @Test
    void shouldExposeSwaggerUI() {
        RestAssured.when().get("/swagger-ui/index.html")
                .then().statusCode(HttpStatus.OK.value())
                .contentType(HTML);
    }

    @Test
    void shouldExposeOpenApiSpec() {
        RestAssured.when().get("/v3/api-docs")
                .then().statusCode(HttpStatus.OK.value())
                .contentType(JSON);
    }
}
