# ACME starters

## Available starters

The following list describes the Spring Boot starters available in this project :

* [`acme-starter`](acme-starter): Base starter to include for any project (directly or transitively).
* [`acme-starter-keycloak`](acme-starter-keycloak): Starter to include when the application protects its endpoints with Keycloak.
* [`acme-starter-postgresql`](acme-starter-postgresql): Starter to include when the application uses a Postgresql database.
* [`acme-starter-rest-api`](acme-starter-rest-api): Starter to include when the application exposes a Rest API.
* [`acme-starter-test`](acme-starter-test): Starter to include when the application is tested (should be always present).

## How to create a new starter

To create a new starter, follow these instructions :

1. create a new Maven submodule in the `acme-starters` module ;
2. add new dependency management into `acme-spring-boot-dependencies` to facilitate usage of this new module ;
```xml
            <dependency>
                <groupId>com.acme</groupId>
                <artifactId>acme-starter-whatelse</artifactId>
                <version>${revision}</version> <!-- ${revision} will be replaced by flatten-maven-plugin -->
            </dependency>
```
3. add starter dependencies into the Maven POM of the new starter ;
4. (optional) add some default properties inside the `src/main/resources/config/application-starter-<your-starter-name>.yml`.
5. (optional) add some Spring Boot autoconfiguration.
