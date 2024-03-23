package com.study.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Configuration
@Slf4j
public class JedisConfiguration {

	@Bean
	public JedisPool jedisPool() {
		return new JedisPool("127.0.0.1", 6379);
	}

	@Bean
	public Jedis jedis(JedisPool jedisPool) {
		return jedisPool.getResource();
	}
}
