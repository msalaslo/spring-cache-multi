package com.msl.cache.springcachemulti.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import lombok.Data;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 * @version 1.0
 * @see
 * @since JDK1.8
 */
@Data
@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    //  Database index used by the connection factory.
    @Value("${spring.redis.database}")
    private int database;

    // Connection URL, will override host, port and password (user will be ignored), e.g. redis://user:password@example.com:6379
    @Value("${spring.redis.url}")
    private String url;

    // Redis server host.
    @Value("${spring.redis.host}")
    private String host;

    // Redis server port.
    @Value("${spring.redis.port}")
    private int port;

    // Login password of the redis server.Redis server password.
    @Value("${spring.redis.password}")
    private String password;

    // Enable SSL support.
    @Value("${spring.redis.ssl}")
    private boolean ssl;

    // Connection timeout in milliseconds.
    @Value("${spring.redis.timeout}")
    private int timeout;
    // Max number of connections that can be allocated by the pool at a given time. Use a negative value for no limit.
    @Value("${spring.redis.pool.max-active}")
    private int poolMaxActive;
    // Max number of "idle" connections in the pool. Use a negative value to indicate an unlimited number of idle connections.
    @Value("${spring.redis.pool.max-idle}")
    private int poolMaxIdle;
    // Maximum amount of time (in milliseconds) a connection allocation should block before throwing an exception when the pool is exhausted. Use a negative value to block indefinitely.
    @Value("${spring.redis.pool.max-wait}")
    private int poolMaxWait;
    // Target for the minimum number of idle connections to maintain in the pool. This setting only has an effect if it is positive.
    @Value("${spring.redis.pool.min-idle}")
    private int poolMinIdle;
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
    public RedisConnectionFactory redisConnectionFactory() {
        if (sentinelEnabled) {
            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
            redisSentinelConfiguration.setMaster(sentinelMaster);
            for (String sentinelNode : sentinelNodes) {
                String sentinelHost = sentinelNode.split(":")[0];
                Integer sentinelPort = Integer.parseInt(sentinelNode.split(":")[1]);
                redisSentinelConfiguration.sentinel(sentinelHost, sentinelPort);
            }
            return new JedisConnectionFactory(redisSentinelConfiguration);
        } else if (clusterEnabled) {
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
            redisClusterConfiguration.setMaxRedirects(clusterMaxRedirects);
            return new JedisConnectionFactory(redisClusterConfiguration);
        } else {            
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setDatabase(database);
            config.setHostName(host);
            config.setPassword(password);
            return new JedisConnectionFactory(config);
//            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//            jedisPoolConfig.setMaxIdle(poolMaxIdle);
//            jedisPoolConfig.setMinIdle(poolMinIdle);
//            jedisPoolConfig.setMaxWaitMillis(poolMaxWait);
//            jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        }
    }

    /**
     * @return
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(redisConnectionFactory());
    }

    /**
     * RedisTemplate is necessary for Redis repositories @see com.hotelbeds.appservicesapiloader.cache.repository.IpRepository for example
     *
     * @return
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    /**
     * Redis cache manager.
     *
     * @return the cache manager
     */
    @Bean(name = "redisCacheManager")
    public CacheManager redisCacheManager() {
        RedisCacheManager localRedisCacheManager = new RedisCacheManager(redisTemplate());
        localRedisCacheManager.setCacheNames(Arrays.asList("REDIS_CACHE_1"));
        localRedisCacheManager.afterPropertiesSet();
        return localRedisCacheManager;
    }
    
//	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
//	    template.setConnectionFactory(redisConnectionFactory());
		template.setConnectionFactory(lettuceConnectionFactory());
		return template;
	}
    
    @Bean
    public RedisMappingContext keyValueMappingContext() {
        return new ContentRedisMappingContext();
    }
}

