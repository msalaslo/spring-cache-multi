package com.msl.cache.springcachemulti.config;

import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomiser;

import java.util.ArrayList;
import java.util.List;

public class VersionedApi extends SpecFilter implements OpenApiCustomiser {

    private final String contextPath;
    private final String version;
    private static final String INFO_API_NAME = "CAMERAS";

    // This is an useful description of the API, at least you must include the name of the Microservice
    private static final String INFO_API_DESCRIPTION = "API for schedule plans";

    private static final String INFO_CONTACT_EMAIL = "SP.DG.PS2@securitasdirect.es";
    private static final String INFO_CONTACT_NAME = "PS2 AS Securitas Direct";
    private static final String INFO_CONTACT_URL = "https://faas.securitasdirect.local/";
    private static final String INFO_LICENSE_NAME = "License of API for Securitas Direct use only";
    private static final String INFO_LICENSE_URL = "https://faas.securitasdirect.local/";

    public VersionedApi(String contextPath, String version) {
        this.contextPath = contextPath;
        this.version = version;
    }

    @Override
    public void customise(OpenAPI openApi) {
        // Overwrite the generated absolute URL with a relative URL to allow requests through "Gateway Microservices".
        // This is supported by WSO2 API Manager because overwrites the URL with its own service URL.
        // Adds the API version to the context path
        // springdoc-openapi-ui: We need a version higher than 1.4.0 to solve this bug: #695 - Servers OpenAPI block resets after customizing with GroupedOpenApi
        List<Server> serverList = new ArrayList<Server>();
        serverList.add(new Server().url(contextPath + "/" + version));
        openApi.setServers(serverList);

        // Force 3.0.0 instead of 3.0.1 for compatibility with WSO2 API Manager 2.6.0
        openApi.setOpenapi("3.0.0");

        // Add authentication option: allows to include user credentials with basic authentication
        // This is useful for securized microservices, we usually include security in "Gateway Microservices"
        Components components = openApi.getComponents();
        if(components == null) {
            components = new Components();
        }
        openApi.setComponents(components.addSecuritySchemes("basicScheme",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")));

        //API information, it will be included automatically in the API Manager publishing phase
        openApi.setInfo(
                new Info()
                        .title(INFO_API_NAME)
                        .description(INFO_API_DESCRIPTION)
                        .version(version)
                        .contact(new Contact()
                                .email(INFO_CONTACT_EMAIL)
                                .name(INFO_CONTACT_NAME)
                                .url(INFO_CONTACT_URL))
                        .license(new License().
                                name(INFO_LICENSE_NAME)
                                .url(INFO_LICENSE_URL)));

        // Remove the version (example: /v1.0) from the API definition paths, we have included it in above setServers method
        // This avoid duplicate the version in the URL when publish the API in the API Manager
        Paths versionedPaths = new Paths();
        openApi.getPaths().entrySet().stream().filter(path -> path.getKey().startsWith("/" + version)).forEach(path -> {
            String key = path.getKey().substring(version.length() + 1);
            versionedPaths.put(key,path.getValue());
        });
        openApi.setPaths(versionedPaths);

        super.removeBrokenReferenceDefinitions(openApi);
    }
}
