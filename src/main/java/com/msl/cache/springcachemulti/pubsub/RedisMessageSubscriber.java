package com.msl.cache.springcachemulti.pubsub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSubscription.Message;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class RedisMessageSubscriber {

	@Autowired
	private CameraService cameraService;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;
	
    @Autowired
    private ChannelTopic topic;

	public static List<String> messageList = new ArrayList<String>();
	
	@PostConstruct
	public void process() {
		Flux<? extends Message<String, String>> stream = redisTemplate.listenTo(topic);

		stream.doOnNext(message -> {
		    // message processing ...
//			onMesage(msg);
			messageList.add(message.toString());
			String stringBodyMessage = new String(message.getMessage().getBytes());
			LOGGER.info("Message received: " + stringBodyMessage);
			List<String> messageCommands = getMessageCommands(stringBodyMessage);
			String action = messageCommands.get(0);
			String id = messageCommands.get(1);
			if (action.equalsIgnoreCase("DELETE")) {
				LOGGER.info("Deleting camera with ID: " + id);
				cameraService.deleteById(id);
			} else if (action.equalsIgnoreCase("UPDATE")) {
				LOGGER.info("Updating camera with ID: " + id);
				cameraService.deleteById(id);
			}
		}).subscribe();
	}

	public List<String> getMessageCommands(String str) {
		return Collections.list(new StringTokenizer(str, "|")).stream().map(token -> (String) token)
				.collect(Collectors.toList());
	}
}
