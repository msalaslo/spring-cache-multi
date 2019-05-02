package com.msl.cache.springcachemulti.domain.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.msl.cache.springcachemulti.domain.entity.Camera;

import lombok.extern.slf4j.Slf4j;

/**
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Slf4j
@Component
public class CameraRedisRepository {

	@Autowired
	private RedisTemplate<String, Camera> redisTemplate;

	public List<Camera> findAll() {
		LOGGER.debug("Retrieving the whole list of application items!");
		return redisTemplate.opsForList().range("test", 0, -1);
	}

	public void save(Camera camera) {
		LOGGER.debug("Application item with ID {} adding",
				camera.getCountry() + camera.getInstallation() + camera.getZone());
		redisTemplate.opsForList().leftPush("test", camera);
	}

}
