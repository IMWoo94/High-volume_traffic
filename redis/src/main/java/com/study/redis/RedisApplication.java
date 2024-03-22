package com.study.redis;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootApplication
public class RedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);

		try(var jedisPool = new JedisPool("127.0.0.1", 6379);){
			try(Jedis jedis = jedisPool.getResource()){
				jedis.set("users:300:name", "lee");
				jedis.set("users:300:age", "20");
				jedis.set("users:300:email", "lee@gmail.com");

				String s = jedis.get("users:300:email");
				System.out.println(s);

				List<String> mget = jedis.mget("users:300:name", "users:300:age", "users:300:email");
				mget.forEach(System.out::println);


			}
		}
	}

}
