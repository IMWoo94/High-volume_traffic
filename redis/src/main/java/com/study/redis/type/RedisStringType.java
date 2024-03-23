package com.study.redis.type;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStringType implements RedisType {

	private final Jedis jedis;

	@Override
	public void run() {
		Pipeline pipelined = jedis.pipelined();
		pipelined.set("users:400:email", "greg@gmail.com");
		pipelined.set("users:400:name", "greg");
		pipelined.set("users:400:age", "20");
		List<Object> objects = pipelined.syncAndReturnAll();
		objects.forEach(it -> log.info(it.toString()));
		// 현재 상태에서는 set, get 이 일어날떄 한번 한번 일어나게 된다.
		// 일괄 처리 할 수 있는 Pipelining 을 적용하자
		jedis.set("users:300:name", "lee");
		jedis.set("users:300:age", "20");
		jedis.set("users:300:email", "lee@gmail.com");

		String s = jedis.get("users:300:email");
		log.info(s);

		List<String> mget = jedis.mget("users:300:name", "users:300:age", "users:300:email");
		mget.forEach(log::info);

		long counter1 = jedis.incr("counter");
		log.info("jedis.incr(\"counter\") {}", counter1);
		long counter2 = jedis.incrBy("counter", 20);
		log.info("jedis.incrBy(\"counter\", 20) {}", counter2);

		long decr = jedis.decr("counter");
		log.info("jedis.decr(\"counter\") {}", decr);
	}

	@Override
	public void reset() {
		Set<String> keys = jedis.keys("*");
		Pipeline pipelined = jedis.pipelined();
		keys.forEach(pipelined::unlink);
		pipelined.syncAndReturnAll();
	}

}
