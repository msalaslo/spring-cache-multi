package com.msl.cache.springcachemulti;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
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
@EnableAdminServer
@Configuration
public class Application implements AsyncConfigurer {
	
	protected Application() {
		LOGGER.info("Starting REST microservice");
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(7);
        executor.setMaxPoolSize(20);
//        executor.setQueueCapacity(11);
        executor.setThreadNamePrefix("AsynExecutor-");
        executor.initialize();
        return executor;
    }

	@Configuration
	public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.authorizeRequests().anyRequest().permitAll()  
	            .and().csrf().disable();
	    }
	}

}
