package com.study.redis.type;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Slf4j
@RequiredArgsConstructor
public class RedisListAndSetType implements RedisType {

	private final Jedis jedis;

	@Override
	public void run() {
		/**
		 * list
		 * 1. stack
		 * 2. queue
		 * 3. block brpop, blpop
		 */

		// stack
		stackRun();

		// queue

	}

	public void stackRun() {
		jedis.rpush("stack1", "aaaa");
		jedis.rpush("stack1", "bbbb");
		jedis.rpush("stack1", "cccc");

		List<String> stack1 = jedis.lrange("stack1", 0, -1);
		stack1.forEach(log::info);

		long len = jedis.llen("stack1");
		while (len != 0) {
			var value = jedis.rpop("stack1");
			log.info("stack1 rpop {} {}", len--, value);
		}
	}

	@Override
	public void reset() {
		Set<String> keys = jedis.keys("*");
		Pipeline pipelined = jedis.pipelined();
		keys.forEach(pipelined::unlink);
		pipelined.syncAndReturnAll();
	}
}
