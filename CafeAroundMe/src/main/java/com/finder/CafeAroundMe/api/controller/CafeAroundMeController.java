package com.finder.CafeAroundMe.api.controller;

import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/openapi/cam")
@Slf4j
@RequiredArgsConstructor
public class CafeAroundMeController {

	private final StringRedisTemplate stringRedisTemplate;
	private final Jedis jedis;

	@GetMapping("/keys")
	public Set<String> getAllKeys() {
		Set<String> keys = jedis.keys("*");
		return keys;
	}

}
