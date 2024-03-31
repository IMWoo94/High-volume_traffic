package com.study.redis.performance.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/performance/commend")
public class RedisSearchPerformanceController {

	private final Jedis jedis;

	@GetMapping("/keys")
	public void commendKeys() {
		long start = System.currentTimeMillis();

		log.info("keys 명령어로 대량 key 정보를 가져오는 경우 싱글 쓰레드 기반으로 지연이 발생 할 수 있다.");
		Set<String> keys = jedis.keys("*");

		long count = keys.size();
		long end = System.currentTimeMillis();
		log.info("total : {}, time : {}", count, end - start);
	}

	@GetMapping("/scan")
	public void commendScan() {

		log.info("scan 명령어로 cursor 형식으로 갯수를 지정해서 가져오면 성능 문제를 개선할 수 있다.");
		ScanResult<String> scanResult;
		String cursor = ScanParams.SCAN_POINTER_START;
		Set<String> keys = new HashSet<>();
		List<Long> times = new ArrayList<>(10000);

		do {
			long start = System.currentTimeMillis();
			scanResult = jedis.scan(cursor, new ScanParams().count(10000).match("*"));
			long end = System.currentTimeMillis();

			keys.addAll(scanResult.getResult());
			cursor = scanResult.getCursor();

			times.add(end - start);
			log.info("cursor : {}", scanResult.getCursor());
		} while (!scanResult.isCompleteIteration());

		long count = keys.size();
		double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
		log.info("total : {}, time : {}", count, avg);
	}

}
