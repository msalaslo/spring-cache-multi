package com.msl.cache.springcachemulti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
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
@EnableAsync
public class Application {

	protected Application() {
		LOGGER.info("Starting REST microservice with Caffeine and Redis as possible caches");
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
