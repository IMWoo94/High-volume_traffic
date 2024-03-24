package com.study.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.study.redis.type.RedisGeospatialType;
import com.study.redis.type.RedisType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JedisConfiguration {

	@Bean
	public JedisPool jedisPool() {
		GenericObjectPoolConfig<Jedis> config = new GenericObjectPoolConfig<>();
		config.setJmxNamePrefix("customJedisPool");
		config.setJmxNameBase("jedisPool");
		config.setJmxEnabled(false);
		String jmxNamePrefix = config.getJmxNamePrefix();
		log.info("JedisConfiguration {}", jmxNamePrefix);
		JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379);
		return jedisPool;
	}

	@Bean
	public Jedis jedis(JedisPool jedisPool) {
		return jedisPool.getResource();
	}

	@Bean
	public RedisType redisType(Jedis jedis) {
		// String type
		// return new RedisStringType(jedis);

		// List type
		// return new RedisListType(jedis);

		// Set type
		// return new RedisSetType(jedis);

		// Hash type
		// return new RedisHashType(jedis);

		// Sorted Set type
		// return new RedisSortedSetType(jedis);

		// Geospatial Type
		return new RedisGeospatialType(jedis);

	}
}
