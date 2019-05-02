package com.msl.cache.springcachemulti.config;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import lombok.Data;

/**
 *
 * @author msalaslo
 */
@Data
@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    //  Database index used by the connection factory.
    @Value("${spring.redis.database}")
    private int database;

    // Redis server host.
    @Value("${spring.redis.host}")
    private String host;

    // Redis server port.
    @Value("${spring.redis.port}")
    private int port;

    // Login password of the redis server.Redis server password.
    @Value("${spring.redis.password}")
    private String password;

    // Maximum number of redirects to follow when executing commands across the cluster.
    @Value("${spring.redis.cluster.max-redirects}")
    private int clusterMaxRedirects;
    
    // Comma-separated list of "host:port" pairs to bootstrap from.
    @Value("${spring.redis.cluster.nodes}")
    private List<String> clusterNodes;
    
    // Name of Redis server.
    @Value("${spring.redis.sentinel.master}")
    private String sentinelMaster;
    
    // Comma-separated list of host:port pairs.
    @Value("${spring.redis.sentinel.nodes}")
    private List<String> sentinelNodes;
    //
    @Value("${spring.redis.pool.enabled}")
    private Boolean poolEnabled;
    //
    @Value("${spring.redis.cluster.enabled}")
    private Boolean clusterEnabled;
    //
    @Value("${spring.redis.sentinel.enabled}")
    private Boolean sentinelEnabled;

    /**
     * Connection factory
     *
     * @return
     */
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        if (sentinelEnabled) {
            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
            redisSentinelConfiguration.setMaster(sentinelMaster);
            redisSentinelConfiguration.setDatabase(database);
            redisSentinelConfiguration.setPassword(password);
            for (String sentinelNode : sentinelNodes) {
                String sentinelHost = sentinelNode.split(":")[0];
                Integer sentinelPort = Integer.parseInt(sentinelNode.split(":")[1]);
                redisSentinelConfiguration.sentinel(sentinelHost, sentinelPort);
            }
            return new LettuceConnectionFactory(redisSentinelConfiguration);
        } else if (clusterEnabled) {
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
            redisClusterConfiguration.setMaxRedirects(clusterMaxRedirects);
            redisClusterConfiguration.setPassword(password);
            return new LettuceConnectionFactory(redisClusterConfiguration);
        } else {            
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setDatabase(database);
            config.setHostName(host);
            config.setPort(port);
            config.setPassword(password);
            return new LettuceConnectionFactory(config);
        }
    }

    /**
     * Redis cache manager.
     *
     * @return the cache manager
     */
    @Bean
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(1))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        redisCacheConfiguration.usePrefix();
       return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory)
                        .cacheDefaults(redisCacheConfiguration).build();
    }
    
    /**
     * Since it is quite common for the keys and values stored in Redis to be java.lang.String, 
     * the Redis modules provides two extensions to RedisConnection and RedisTemplate.
     * For String intensive operations consider the dedicated StringRedisTemplate.
     * @return StringRedisTemplate
     */
	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate template = new StringRedisTemplate();
//	    template.setConnectionFactory(redisConnectionFactory());
		template.setConnectionFactory(lettuceConnectionFactory());
		return template;
	}
    
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
//	    template.setConnectionFactory(redisConnectionFactory());
		template.setConnectionFactory(lettuceConnectionFactory());
		return template;
	}
	
	
}

