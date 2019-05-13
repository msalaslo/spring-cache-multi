package com.msl.cache.springcachemulti.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.Data;

/**
 * @author msalaslo
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.cache.caffeine")
@Validated
public class CaffeineCacheConfiguration {

	/** The parameter to activate or deactivate the caffeine cache. */
	public boolean active;

	/** The configurations. */
	@Valid
	private List<CaffeineConfiguration> configurations;

	/**
	 * Instantiates a new Caffeine configuration.
	 */
	@Data
	public static class CaffeineConfiguration {

		/** The cache name. */
		private String cacheName;

		/** The initial capacity. */
		private Integer initialCapacity;

		/** The maximum size. */
		private Integer maximumSize;

		/** The maximum weight. */
		private Integer maximumWeight;

		/** The expire after access. */
		private Integer expireAfterAccess;

		/** The expire after write. */
		private Integer expireAfterWrite;

		/** The refresh after write. */
		private Integer refreshAfterWrite;
	}

	/**
	 * Caffeine cache manager.
	 *
	 * @return the cache manager
	 */
	@Bean(name = "caffeineCacheManager")
	public CacheManager caffeineCacheManager() {
		List<CaffeineCache> cacheList = new ArrayList<>();
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		for (CaffeineCacheConfiguration.CaffeineConfiguration configuration : configurations) {
			Caffeine<Object, Object> chacheBuilder = Caffeine.newBuilder();
			if (configuration.getInitialCapacity() != null) {
				chacheBuilder.initialCapacity(configuration.getInitialCapacity());
			}
			if (configuration.getMaximumSize() != null) {
				chacheBuilder.maximumSize(configuration.getMaximumSize());
			}
			if (configuration.getMaximumWeight() != null) {
				chacheBuilder.maximumWeight(configuration.getMaximumWeight());
			}
			if (configuration.getExpireAfterAccess() != null) {
				chacheBuilder.expireAfterAccess(configuration.getExpireAfterAccess(), TimeUnit.SECONDS);
			}
			if (configuration.getExpireAfterWrite() != null) {
				chacheBuilder.expireAfterWrite(configuration.getExpireAfterWrite(), TimeUnit.SECONDS);
			}
			if (configuration.getRefreshAfterWrite() != null) {
				chacheBuilder.refreshAfterWrite(configuration.getRefreshAfterWrite(), TimeUnit.SECONDS);
			}
			CaffeineCache caffeineCache = new CaffeineCache(configuration.getCacheName(), chacheBuilder.build());
			cacheList.add(caffeineCache);
		}
		cacheManager.setCaches(cacheList);
		return cacheManager;
	}
}
