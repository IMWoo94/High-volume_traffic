package com.study.redis.type;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.resps.Tuple;

@Slf4j
@RequiredArgsConstructor
public class RedisSortedSetType implements RedisType {

	private final Jedis jedis;
	private final String keyName = "game1:scores";

	@Override
	public void run() {

		// sorted set prefix : Z
		long chResult = jedis.zadd(keyName, 100, "user1");

		HashMap<String, Double> scores = new HashMap<>() {{
			put("user2", 100.0);
			put("user3", 120.0);
			put("user4", 560.0);
			put("user5", 50.0);
			put("user6", 20.0);
		}};

		jedis.zadd(keyName, scores);

		// range default 오름차순
		log.info("zrange");
		List<String> zrange = jedis.zrange(keyName, 0, Long.MAX_VALUE);
		zrange.forEach(log::info);

		log.info("zrangeWithScores");
		List<Tuple> tuples = jedis.zrangeWithScores(keyName, 0, Long.MAX_VALUE);
		tuples.forEach(i -> log.info("{} : {}", i.getElement(), i.getScore()));

		log.info("zrangeByScoreWithScores");
		List<Tuple> tuples1 = jedis.zrangeByScoreWithScores(keyName, 50, 200);
		tuples1.forEach(i -> log.info("{} : {}", i.getElement(), i.getScore()));

		jedis.zincrby(keyName, 21.0, "user2");

		// 내림차순
		log.info("zrangeByScoreWithScores");
		List<Tuple> tuples2 = jedis.zrevrangeByScoreWithScores(keyName, Long.MAX_VALUE, 0, 1, 3);
		tuples2.forEach(i -> log.info("{} : {}", i.getElement(), i.getScore()));

	}

	@Override
	public void reset() {
		Set<String> keys = jedis.keys("*");
		Pipeline pipelined = jedis.pipelined();
		keys.forEach(pipelined::unlink);
		pipelined.syncAndReturnAll();
	}

}
