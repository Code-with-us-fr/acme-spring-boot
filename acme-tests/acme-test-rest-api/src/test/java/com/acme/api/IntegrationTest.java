package com.acme.api;

import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:reset.sql"})
@ImportTestcontainers(IntegrationTest.Containers.class)
@AutoConfigureObservability(tracing = false) // To expose /actuator/prometheus endpoint
public @interface IntegrationTest {

    interface Containers {

        @Container
        @ServiceConnection
        PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:16");
    }
}
