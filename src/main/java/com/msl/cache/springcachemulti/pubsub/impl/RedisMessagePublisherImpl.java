package com.msl.cache.springcachemulti.pubsub.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.pubsub.MessagePublisher;

@Service
@ConditionalOnProperty(value = "spring.cache.redis.active", havingValue = "true")
public class RedisMessagePublisherImpl implements MessagePublisher {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private ChannelTopic topic;

	public RedisMessagePublisherImpl() {
	}

	public RedisMessagePublisherImpl(final RedisTemplate<String, String> redisTemplate, final ChannelTopic topic) {
		this.redisTemplate = redisTemplate;
		this.topic = topic;
	}

	public void publish(final String message) {
		redisTemplate.convertAndSend(topic.getTopic(), message);
	}
}
