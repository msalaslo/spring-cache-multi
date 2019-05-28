package com.msl.cache.springcachemulti.config;

import java.util.concurrent.Executor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "async.executor")
public class AsyncConfiguration implements AsyncConfigurer {
	
	private int corePoolSize;
	private int maxPoolSize;
	private int queueCapacity;
	
	@Bean(name = "customAsyncExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("AsynExecutor-");
        executor.initialize();
        LOGGER.info("AsyncExecutor initialized corePoolSize {}, maxPoolSize {}, queueCapacity {}", corePoolSize, maxPoolSize, queueCapacity);
        return executor;
    }

}
