package com.msl.cache.springcachemulti.reactive;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.msl.cache.springcachemulti.domain.entity.Camera;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class RedisReactiveServiceTest {

	@Autowired
	RedisReactiveService redisReactiveService;

	@Test
	public void givenCampmera_whenSet_thenSet() {
		Date time = new Date();
		Camera camera = new Camera("S123", "I123", "ESP", "I001", "Z001", "P123", "A123", time, time, "V1");
		Mono<Boolean> result = redisReactiveService.saveBySerial(camera);
		StepVerifier.create(result).expectNext(true).verifyComplete();
	}

}
