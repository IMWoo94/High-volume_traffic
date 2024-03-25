package com.finder.CafeAroundMe.api.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finder.CafeAroundMe.api.domain.CafeLocation;
import com.finder.CafeAroundMe.api.service.KakaoOpenApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@RestController
@RequestMapping("/openapi/cam")
@Slf4j
@RequiredArgsConstructor
public class CafeAroundMeController {

	private final Jedis jedis;
	private final KakaoOpenApiService kakaoOpenApiService;

	// Redis key info get
	@GetMapping("/keys")
	public Set<String> getAllKeys() {
		Set<String> keys = jedis.keys("*");
		return keys;
	}

	// Redis Cafe Location info add
	@GetMapping("/location/add")
	public void getLocationInfoAdd() {
		// category_group_code=CE7 [ 카페 ]
		// 2000m 주변 카페 카테고리 리스트 API 호출
		CafeLocation cafeLocation = kakaoOpenApiService.getCafeLocationInfo(15, 1);

		String key = "cafeLocation";
		Pipeline pipelined = jedis.pipelined();
		cafeLocation.getLocations().forEach(it -> {
			log.info("locations :{}", it);
			pipelined.geoadd(key, it.getX(), it.getY(), it.getPlaceName());
		});
		pipelined.sync();
	}

}
