package com.msl.cache.springcachemulti.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.pubsub.MessagePublisher;
import com.msl.cache.springcachemulti.service.CameraService;
import com.msl.cache.springcachemulti.service.CameraServicePubSub;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CameraServicePubSubImpl implements CameraServicePubSub {

	@Autowired
	CameraService cameraService;
	
    @Autowired    
    private MessagePublisher messagePublisher;

    @Override    
	public void deleteById(String id) {
		LOGGER.debug("deleteById, publishing in Redis:" + id);
		cameraService.deleteById(id);
		//TODO this is a very simple message
		String deleteMessage = "DELETE|" + id;
		messagePublisher.publish(deleteMessage);
	}
	
}
