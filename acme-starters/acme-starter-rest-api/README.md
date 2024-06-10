# ACME Starter Rest API

This starter provides these following features :

* serve management endpoints to the dedicated port `9090`
  * expose liveness (`/actuator/health/liveness`) and readiness (`/actuator/health/readiness`) probes
  * expose a Prometheus endpoint (`/actuator/prometheus`)
* serve OpenAPI 3 docs (`/v3/api-docs`) and swagger-ui (`/swagger-ui.html`)

## Getting started

To use this starter, add this dependency to your Maven POM :

```xml
<dependency>
    <groupId>com.acme</groupId>
    <artifactId>acme-starter-rest-api</artifactId>
</dependency>
```
