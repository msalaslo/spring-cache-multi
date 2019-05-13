package com.msl.cache.springcachemulti.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CacheManagerConfiguration {

    /**
     * The caffeine cache manager.
     */
    @Autowired
    private CacheManager caffeineCacheManager;
    
    /**
     * Caffeine cache configuration
     */
    @Autowired
    private CaffeineCacheConfiguration caffeineCacheConfiguration;

    /**
     * The redis cache manager.
     */
    @Autowired
    private CacheManager redisCacheManager;

    /**
     * Cache manager.
     *
     * @return the cache manager
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
    	if (caffeineCacheConfiguration.isActive()) {
            // Es importante el orden que se añaden los cache manager al CompositeCacheManager.
            // En caso de que compartan la misma CACHE_NAME, por defecto tomara la primera cacheManager añadida.
    		return new CompositeCacheManager(caffeineCacheManager, redisCacheManager);
    	} else {
    		return new CompositeCacheManager(redisCacheManager);
    	}
    }
}

