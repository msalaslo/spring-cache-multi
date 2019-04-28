# The Spring-cache-multi Project

## About

Multi Layer cache (caffeine, redis) with Spring cache

## Technical Stack:

- Java 1.8+
- Maven 3.3+
- Spring boot 1.5.10
    - Spring Boot Actuator for exposing management endopoints
- Spring cache
- Spring cloud Edgware.SR3
    - Spring Cloud Sleuth for distributed tracing
- Lombok abstraction
- Mapstruct for bean mapping
- Swagger 2 API documentation (available at /swagger-ui.html)
- REST API model validation 
- Cucumber and Spring Boot test for test
- Logback for logging
    - Log patterns for local and cloud SPRING profiles includes Sleuth headers

## Installation
This application is configured with two SPRING profiles:
- "local": For local dev environments.
- "cloud": For dockerized environments, where application properties are set by ENV variables.

Test on the browser via SWAGGER
-------------------

```sh
http://localhost:8080/swagger-ui.html
```