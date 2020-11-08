package com.msl.cache.springcachemulti.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Collections;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration that generates one swagger Docket per API version.
 * Additionally, generates docklets removing the API from the URL. 
 * The enable an easy integration with API Management tools like WSO2 API Manager.
 * 
 * API version is identified by the operation endpoint URL (e.g: /v1.0/resource/...)
 * API version is specified in the Controller request mapping (e.g.: @GetMapping("/v1.0/resource/...")).
 * API version is removed from the operations URL and added on the base path (context)
 * 
 * API documentation is available at /swagger-ui.html
 *
 * @since 1.0.0
 * @author FaaS
 */
@Configuration
@EnableSwagger2
public class VersionedAPISwaggerConfiguration {
	
	@Autowired
	private ServletContext servletContext;
	
	private static final String GROUP_NAME = "cache-service";
	private static final String BASE_PACKAGE = "com.msl.cache.springcachemulti.api";
	private static final String APIM_PREFIX = "APIM";
	private static final String V10 = "v1.0";
	
	/**
	 * Docket that collects API operations of v1.0 version
	 * @return swagger docker
	 */
    @Bean
    public Docket v10DocumentationPlugin() {
    	String version = V10;
    	String context = servletContext.getContextPath() + "/" + version;
    	String name = GROUP_NAME + "-" + version;
        return new VersionedDocket(name, version, context);
    }
  
	/**
	 * Docket that collects API operations of v1.0 version
	 * @return swagger docker
	 */
    @Bean
    public Docket v10WSO2DocumentationPlugin() {
    	String version = V10;
    	String context = servletContext.getContextPath();
    	String name = APIM_PREFIX + "-" + GROUP_NAME + "-" + version;
        return new VersionedDocket(name, version, context);
    } 

    class VersionedDocket extends Docket {
        public VersionedDocket(String groupName, String version, String context) {
            super(DocumentationType.SWAGGER_2);
            super.groupName(groupName)
            .select()
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
				.paths(regex("/" + version + ".*"))
                .build()
            .apiInfo(getApiInfo(version))
            .pathProvider(new RemoveAPIVersionFromOperationUrlsPathProvider(context, version))
            .useDefaultResponseMessages(false)
            .enableUrlTemplating(true);
        }

    	private ApiInfo getApiInfo(String version) {
    		return new ApiInfo("Double Cache Layer API", "Double Cache Layer  API documentation", version,
    				"Terms of service based into company terms of use",
    				new Contact("MSALASLO", "https://github.com/msalaslo",
    						"msalaslo@gmail.com"),
    				"", 
    				"",
    				Collections.emptyList());
    	}
    }

    /**
     * Custom implementation that allow us seamless integration with API Managements tools (WSO2 API Manager)
     * Sets the application base path (it will be used in the endpoint invocation)
     * Removes the version from the operation URLs
     */
    class RemoveAPIVersionFromOperationUrlsPathProvider extends AbstractPathProvider {
        private String basePath;
        private String version;

        public RemoveAPIVersionFromOperationUrlsPathProvider(String basePath, String version) {
            this.basePath = basePath;
            this.version = version;
        }

        @Override
        protected String applicationPath() {
            return basePath;
        }

        @Override
        protected String getDocumentationPath() {
            return "/";
        }

        @Override
        public String getOperationPath(String operationPath) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
            return Paths.removeAdjacentForwardSlashes(
                    uriComponentsBuilder.path(operationPath.replaceFirst(version, "")).build().toString());
        }
    }
}
