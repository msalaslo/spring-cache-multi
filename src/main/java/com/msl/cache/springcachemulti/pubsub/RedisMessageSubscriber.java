package com.msl.cache.springcachemulti.pubsub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisMessageSubscriber implements MessageListener {
	
	@Autowired
	private CameraService cameraService;

    public static List<String> messageList = new ArrayList<String>();

    public void onMessage(final Message message, final byte[] pattern) {
        messageList.add(message.toString());
        String stringBodyMessage = new String(message.getBody());
        LOGGER.info("Message received: " + stringBodyMessage);
        List<String> messageCommands = getMessageCommands(stringBodyMessage);
        String id = messageCommands.get(1);
        LOGGER.info("Deleting camera with ID: " + id);
        cameraService.deleteById(id);
    }
    
    public List<String> getMessageCommands(String str) {
        return Collections.list(new StringTokenizer(str, "|")).stream()
          .map(token -> (String) token)
          .collect(Collectors.toList());
    }
}
