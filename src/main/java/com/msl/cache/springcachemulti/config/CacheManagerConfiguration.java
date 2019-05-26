package com.msl.cache.springcachemulti.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.msl.cache.springcachemulti.cache.TwoLayerCacheManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CacheManagerConfiguration {

	// Activate two level cache
	@Value("${spring.cache.two-layer}")
	private boolean twoLayer;

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
	 * Cache manager.
	 *
	 * @return the cache manager
	 */
	@Bean
	@Primary
	@Qualifier("cacheManager")
	public CacheManager cacheManager() {
		if (twoLayer) {
			if (caffeineCacheConfiguration.isActive()) {
				LOGGER.info("Two layer cache activated: CAFFEINE and REDIS");
				return new TwoLayerCacheManager(caffeineCacheManager, redisCacheManager);
			} else if (hazelcastCacheConfiguration.isActive()) {
				LOGGER.info("Two layer cache activated: HAZELCAST and REDIS");
				return new TwoLayerCacheManager(hazelcastCacheManager, redisCacheManager);
			} else {
				String msg = "Two layer cache config activated, but no near cache ativated. Please activate one near cache (caffeine, hazelcast)";
				LOGGER.warn(msg);
				throw new RuntimeException(msg);
			}
		} else {
			LOGGER.info("Two layer cache deactivated using only REDIS");
			return new CompositeCacheManager(redisCacheManager);
		}
	}
}
