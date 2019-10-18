package com.msl.cache.springcachemulti.pubsub.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.pubsub.MessagePublisher;

@Service
@ConditionalOnProperty(value = "spring.cache.redis.active", havingValue = "false")
public class DummyMessagePublisherImpl implements MessagePublisher {

	public DummyMessagePublisherImpl() {
	}

	public DummyMessagePublisherImpl(final RedisTemplate<String, String> redisTemplate, final ChannelTopic topic) {
		// do nothing
	}

	public void publish(final String message) {
		// do nothing
	}
}
