package com.study.redis.type;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Slf4j
@RequiredArgsConstructor
public class RedisSetType implements RedisType {

	private final Jedis jedis;

	@Override
	public void run() {

		// Set
		long setAddCount = jedis.sadd("user:500:follow", "100", "200", "300", "400");
		log.info("user:500:follow add count {}", setAddCount);

		long result = jedis.srem("user:500:follow", "100");
		if (result == 1) {
			log.info("user:500:follow remove complete");
		}

		Set<String> userInfo = jedis.smembers("user:500:follow");
		userInfo.forEach(log::info);

		boolean isMember = jedis.sismember("user:500:follow", "200");
		boolean nonMember = jedis.sismember("user:500:follow", "0");
		log.info("isMember {} , nonMember {}", isMember, nonMember);

		// s card Set 원소 갯수
		long setCount = jedis.scard("user:500:follow");
		log.info("setCount : {} ", setCount);

		// s inter 중복 데이터 확인
		jedis.sadd("user:100:follow", "100", "200");

		Set<String> sinter = jedis.sinter("user:100:follow", "user:500:follow");
		sinter.forEach(log::info);

	}

	@Override
	public void reset() {
		Set<String> keys = jedis.keys("*");
		Pipeline pipelined = jedis.pipelined();
		keys.forEach(pipelined::unlink);
		pipelined.syncAndReturnAll();
	}

}
