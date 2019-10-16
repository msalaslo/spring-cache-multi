package com.msl.cache.springcachemulti.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.msl.cache.springcachemulti.cache.TwoLayerCacheManagerImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CacheManagerConfiguration {

	/**
	 * The caffeine cache manager.
	 */
	@Autowired
	@Qualifier("caffeineCacheManager")
	private CacheManager caffeineCacheManager;

	/**
	 * The redis cache manager.
	 */
	@Autowired
	@Qualifier("redisCacheManager")
	private CacheManager redisCacheManager;

	/**
	 * The redis cache manager.
	 */
	@Autowired
	@Qualifier("hazelcastCacheManager")
	private CacheManager hazelcastCacheManager;

	/**
	 * Caffeine cache configuration
	 */
	@Autowired
	private CaffeineCacheConfiguration caffeineCacheConfiguration;

	/**
	 * Hazelcast cache configuration
	 */
	@Autowired
	private HazelcastCacheConfiguration hazelcastCacheConfiguration;
	
	/**
	 * RedisCacheConfiguration cache configuration
	 */
	@Autowired
	private RedisConfiguration redisCacheConfiguration;

	/**
	 * Cache manager.
	 *
	 * @return the cache manager
	 */
	@Bean
	@Primary
	@Qualifier("cacheManager")
	public CacheManager cacheManager() {
		TwoLayerCacheManagerImpl cacheManager = new TwoLayerCacheManagerImpl();
		if (caffeineCacheConfiguration.isActive()) {
			LOGGER.info("Cache activated: CAFFEINE");
			cacheManager.addNearLayer(caffeineCacheManager);			
		} else if (hazelcastCacheConfiguration.isActive()) {
			LOGGER.info("Cache activated: HAZELCAST");
			cacheManager.addNearLayer(hazelcastCacheManager);		
		} 
		if (redisCacheConfiguration.isActive()) {
			LOGGER.info("Cache activated: REDIS");
			cacheManager.addRemoteLayer(hazelcastCacheManager);	
		}
		return cacheManager;
	}
}
