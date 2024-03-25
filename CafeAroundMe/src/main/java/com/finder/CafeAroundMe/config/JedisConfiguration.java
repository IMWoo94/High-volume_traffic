package com.finder.CafeAroundMe.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Configuration
public class JedisConfiguration {

	@Bean
	public JedisPool jedisPool() {
		GenericObjectPoolConfig<Jedis> jedisGenericObjectPoolConfig = new GenericObjectPoolConfig<>();
		jedisGenericObjectPoolConfig.setJmxEnabled(false);
		return new JedisPool(jedisGenericObjectPoolConfig);
	}

	@Bean
	public Jedis jedis(JedisPool jedisPool) {
		return jedisPool.getResource();
	}
}
