package com.msl.cache.springcachemulti.config;

import java.util.List;

import javax.validation.Valid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.cache.hazelcast")
@Validated
public class HazelcastCacheConfiguration {
	
	/** The parameter to activate or deactivate the Hazelcast cache. */
	public boolean active;

	/** The configurations. */
	@Valid
	private List<HazelcastConfiguration> configurations;

	@Bean(name = "hazelcastCacheManager")
	public CacheManager hazelcastCacheManager() {
		return new HazelcastCacheManager(hazelcastInstance());
	}

	@Bean
	HazelcastInstance hazelcastInstance() {
		return Hazelcast.newHazelcastInstance();
	}

	/**
	 * Instantiates a new Hazelcast configuration.
	 */
	@Data
	public static class HazelcastConfiguration {

		/** The cache name. */
		private String cacheName;

		/** The initial capacity. */
		private Integer timeToLiveSeconds;

		/** The maximum size. */
		private Integer maximumSize;

		/** The expire after access. */
		private Integer maxIdleSeconds;
	}

	@Bean
	public Config hazelCastConfig() {
		Config config = new Config();
		config.setInstanceName("hazelcast-cache");
		for (HazelcastCacheConfiguration.HazelcastConfiguration configuration : configurations) {
			MapConfig mapConfig = new MapConfig();
			mapConfig.setName(configuration.getCacheName());
			mapConfig.setEvictionPolicy(EvictionPolicy.LFU);
			if (configuration.getTimeToLiveSeconds() != null) {
				mapConfig.setTimeToLiveSeconds(configuration.getTimeToLiveSeconds());
			}
			if (configuration.getMaximumSize() != null) {
				mapConfig.getMaxSizeConfig().setSize(configuration.getMaximumSize());
			}
			if (configuration.getMaxIdleSeconds() != null) {
				mapConfig.setMaxIdleSeconds(configuration.getMaxIdleSeconds());
			}
			config.getMapConfigs().put(configuration.getCacheName(), mapConfig);
		}
		return config;
	}
}
