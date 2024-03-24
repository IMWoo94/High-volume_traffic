package com.study.redis.type;

import java.util.Set;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Slf4j
@RequiredArgsConstructor
public class RedisBitmapType implements RedisType {

	private final Jedis jedis;

	@Override
	public void run() {

		// Bitmap
		jedis.setbit("request-somepage", 1, true);
		jedis.setbit("request-somepage", 2, true);
		jedis.setbit("request-somepage", 3, true);

		long bitcount = jedis.bitcount("request-somepage");
		log.info("page request count {}", bitcount);

		log.info(" exists {}", jedis.getbit("request-somepage", 1));
		log.info(" non exists {}", jedis.getbit("request-somepage", 300));

		bitmapAndSetMemoryTest();
	}

	private void bitmapAndSetMemoryTest() {
		Pipeline pipelined = jedis.pipelined();
		IntStream.rangeClosed(0, 100000).forEach(
			i -> {
				pipelined.sadd("request:somepage:set", String.valueOf(i), "1");
				pipelined.setbit("request:somepage:bitmap", i, true);

				if (i == 100) {
					pipelined.sync();
				}
			}
		);
		pipelined.sync();

		Long setMemory = jedis.memoryUsage("request:somepage:set");
		Long bitmapMemory = jedis.memoryUsage("request:somepage:bitmap");
		log.info("usage memory set {} bitmap {}", setMemory, bitmapMemory);
	}

	@Override
	public void reset() {
		Set<String> keys = jedis.keys("*");
		Pipeline pipelined = jedis.pipelined();
		keys.forEach(pipelined::unlink);
		pipelined.syncAndReturnAll();
	}

}
