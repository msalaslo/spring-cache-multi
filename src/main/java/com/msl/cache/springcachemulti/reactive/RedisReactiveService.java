package com.msl.cache.springcachemulti.reactive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;

import com.msl.cache.springcachemulti.domain.entity.Camera;

import reactor.core.publisher.Mono;

public class RedisReactiveService {

	@Autowired
	private ReactiveRedisTemplate<String, Camera> cameraRedisTemplate;
	private ReactiveRedisTemplate<String, String> stringRedisTemplate;

	private ReactiveListOperations<String, String> reactiveListOps;
	private ReactiveValueOperations<String, Camera> reactiveValueOps;
	
	public RedisReactiveService() {
		reactiveValueOps = cameraRedisTemplate.opsForValue();
		reactiveListOps = stringRedisTemplate.opsForList();
	}
	
	public Mono<Camera> findBySerial(String serial) {
		Mono<Camera> camera = reactiveValueOps.get(serial);
		return camera;
	}

	public Mono<Boolean> saveBySerial(Camera camera) {
		Mono<Boolean> result = reactiveValueOps.set(camera.getSerial(), camera);
		return result;
	}

}
