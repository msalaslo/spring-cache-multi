package com.msl.cache.springcachemulti.config;

import javax.validation.constraints.NotNull;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfiguration configuration. API documentation is available at /swagger-ui.html
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Configuration
public class OpenAPIConfiguration {
    
    @NotNull
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public GroupedOpenApi v10Api() {
        return getGroupedApiByVersion("v1.0");
    }

    private GroupedOpenApi getGroupedApiByVersion(String version) {
        return GroupedOpenApi.builder()
                .group(version)
                .addOpenApiCustomiser(new VersionedApi(contextPath, version))
                .build();
    }
    
}
