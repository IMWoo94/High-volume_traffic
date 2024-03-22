package com.study.redis;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

@SpringBootApplication
public class RedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);

		try(var jedisPool = new JedisPool("127.0.0.1", 6379);){
			try(Jedis jedis = jedisPool.getResource()){
				Pipeline pipelined = jedis.pipelined();
				pipelined.set("users:400:email", "greg@gmail.com");
				pipelined.set("users:400:name", "greg");
				pipelined.set("users:400:age", "20");
				List<Object> objects = pipelined.syncAndReturnAll();
				objects.forEach(System.out::println);
				// 현재 상태에서는 set, get 이 일어날떄 한번 한번 일어나게 된다.
				// 일괄 처리 할 수 있는 Pipelining 을 적용하자
				jedis.set("users:300:name", "lee");
				jedis.set("users:300:age", "20");
				jedis.set("users:300:email", "lee@gmail.com");

				String s = jedis.get("users:300:email");
				System.out.println(s);

				List<String> mget = jedis.mget("users:300:name", "users:300:age", "users:300:email");
				mget.forEach(System.out::println);

				long counter1 = jedis.incr("counter");
				System.out.println(counter1);
				long counter2 = jedis.incrBy("counter", 20);
				System.out.println(counter2);

				long decr = jedis.decr("counter");
				System.out.println(decr);



			}
		}
	}

}
