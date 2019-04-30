package com.msl.cache.springcachemulti.config;

import java.time.Duration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

//@EnableCaching
//@Configuration
public class RedisConfiguration2 {

//	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		return new JedisConnectionFactory(config);
	}
	
//	@Bean
	public RedisConnectionFactory lettuceConnectionFactory() {
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
//	    .useSsl().and()
				.commandTimeout(Duration.ofSeconds(2)).shutdownTimeout(Duration.ZERO).build();
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(), clientConfig);
	}

//	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
//	    template.setConnectionFactory(redisConnectionFactory());
		template.setConnectionFactory(lettuceConnectionFactory());
		return template;
	}

}
