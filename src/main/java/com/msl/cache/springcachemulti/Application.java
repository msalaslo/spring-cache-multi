package com.msl.cache.springcachemulti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.extern.slf4j.Slf4j;

/**
 * Application bootstrap class.
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Slf4j
@SpringBootApplication
@EnableCaching
@EnableRedisRepositories
@EnableSpringDataWebSupport
@EnableAsync
public class Application {

	protected Application() {
		LOGGER.info("Starting REST microservice");
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
