package com.msl.cache.springcachemulti.config;

import static com.google.common.base.Predicates.not;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration.  API documentation is available at /swagger-ui.html
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(regex("/.*"))
                .paths(regex("^/(?!env|restart|pause|resume|refresh).*$"))
                .apis(not(basePackage("org.springframework.boot")))              
                	.build()
                .apiInfo(apiInfo());
    }

    // TODO (FaaS) Change in order to adapt to the actual microservice REST API.
    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Application REST API",
                "Application manager REST API documentation.",
                "1.0",
                "Terms of service based into company terms of use",
                new Contact("FaaS Securitas Direct", "https://faas.securitasdirect.local/", "faas@securitasdirect.es"),
                "License of API for YourCompany use only", 
                "License of API for YourCompany use only", 
                Collections.emptyList());
        return apiInfo;
    }
}
