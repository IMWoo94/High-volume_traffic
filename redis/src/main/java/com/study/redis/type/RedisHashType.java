package com.study.redis.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Slf4j
@RequiredArgsConstructor
public class RedisHashType implements RedisType {

	private final Jedis jedis;
	private final String keyName = "users:1:info";

	@Override
	public void run() {

		// Hash

		// 단순 String key-value 형식으로 입력
		jedis.hset(keyName, "name", "lee");

		// Map 컬렉션에 담아서 등록
		Map<String, String> map = new HashMap<>() {{
			put("phone", "010-0000-0000");
			put("email", "lee@gmail.com");
		}};
		jedis.hset(keyName, map);

		String name = jedis.hget(keyName, "name");
		log.info("hashType users:1:info name value : {}", name);

		// hdel
		long len = jedis.hlen(keyName);
		log.info("hash del before : {}", len);
		jedis.hdel(keyName, "phone");
		len = jedis.hlen(keyName);
		log.info("hash del after : {}", len);

		// getAll
		Map<String, String> result = jedis.hgetAll(keyName);
		log.info("hash type all get : {}", result);

		// hincr
		jedis.hincrBy(keyName, "visited", 1);
		Map<String, String> userInfo = jedis.hgetAll(keyName);
		log.info("hash type all get : {}", userInfo);

	}

	@Override
	public void reset() {
		Set<String> keys = jedis.keys("*");
		Pipeline pipelined = jedis.pipelined();
		keys.forEach(pipelined::unlink);
		pipelined.syncAndReturnAll();
	}

}
