package com.msl.cache.springcachemulti.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.pubsub.RedisMessagePublisher;
import com.msl.cache.springcachemulti.service.CameraService;
import com.msl.cache.springcachemulti.service.CameraServicePubSub;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CameraServicePubSubImpl implements CameraServicePubSub {

	@Autowired
	CameraService cameraService;
	
    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @Override
	public void deleteById(String id) {
		LOGGER.debug("deleteById, publishing in Redis:" + id);
		String deleteMessage = "DELETE|" + id;
		redisMessagePublisher.publish(deleteMessage);
		cameraService.deleteById(id);
	}
	
    @Override
	public CameraDTO update(CameraDTO camera, String id) {
		LOGGER.debug("update, publishing in Redis:" + id);
		String message = "UPDATE|" + id;
		redisMessagePublisher.publish(message);
		return cameraService.update(camera, id);
	}
}
