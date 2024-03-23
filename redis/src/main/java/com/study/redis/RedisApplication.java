package com.study.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.study.redis.type.RedisStringType;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootApplication
public class RedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);

		try (JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);) {
			try (Jedis jedis = jedisPool.getResource();) {

				// redis String type
				RedisStringType redisStringType = new RedisStringType(jedis);
				redisStringType.run();

				// redis List ( stack, queue, block )
				// new RedisListAndSetType(jedis);
			}

		}

	}

}
