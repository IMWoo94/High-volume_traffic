package com.study.redis.transactions;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

@Slf4j
@RequiredArgsConstructor
public class RedisTransaction implements RTransaction {

	private final Jedis jedis;

	@Override
	public void transaction() {
		case1();
		case2();
		case3();
	}

	public void case1() {
		log.info("redis transaction test case");

		Transaction multi = jedis.multi();
		multi.set("key1", "100");
		multi.set("key2", "200");
		try {
			log.info("redis transaction not complete ");

			jedis.get("key1");
		} catch (IllegalStateException e) {
			log.info("Error : {}", e);
		}
		multi.exec();

		log.info("redis transaction complete ");
		List<String> mget = jedis.mget("key1", "key2");
		for (String s : mget) {
			log.info(s);
		}
	}

	public void case2() {
		log.info("redis transaction discard test case");

		jedis.set("key", "100");
		Transaction multi = jedis.multi();
		multi.set("key", "200");
		multi.discard();

		log.info("transaction set key : 200 but discard");
		log.info("key : {}", jedis.get("key"));
	}

	public void case3() {
		log.info("redis transaction watch 'key' test case");

		String key = jedis.watch("key");
		Transaction multi = jedis.multi();
		multi.set("key", "555555");

		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			log.info("Error : {}", e);
		}
		List<Object> exec = multi.exec();
		log.info(exec.toString());
	}

	@Override
	public void reset() {
		Set<String> keys = jedis.keys("*");
		Pipeline pipelined = jedis.pipelined();
		keys.forEach(pipelined::unlink);
		pipelined.syncAndReturnAll();
	}
}
