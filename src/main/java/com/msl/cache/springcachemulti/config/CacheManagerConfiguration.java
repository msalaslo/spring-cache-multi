package com.msl.cache.springcachemulti.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCaching
public class CacheManagerConfiguration {

//    /**
//     * The caffeine cache manager.
//     */
//    @Autowired
//    private CacheManager caffeineCacheManager;

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
        // Es importante el orden que se añaden los cache manager al CompositeCacheManager.
        // En caso de que compartan la misma CACHE_NAME, por defecto tomara la primera cacheManager añadida.
//        return new CompositeCacheManager(caffeineCacheManager, redisCacheManager);
    	return new CompositeCacheManager(redisCacheManager);
    }
}

