package com.finder.CafeAroundMe.api.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finder.CafeAroundMe.api.common.annotation.LongLatLocation;
import com.finder.CafeAroundMe.api.domain.RequestLocation;
import com.finder.CafeAroundMe.api.service.CafeAroundMeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.resps.GeoRadiusResponse;

@RestController
@RequestMapping("/open-api/cam")
@Slf4j
@RequiredArgsConstructor
public class CafeAroundMeController {

	private final CafeAroundMeService cafeAroundMeService;

	// Redis key info get
	@GetMapping("/findAllKeys")
	public ResponseEntity<Set<String>> findAllKeys() {
		Set<String> keys = cafeAroundMeService.findAllKeysAndPattern("*");
		return new ResponseEntity<>(keys, HttpStatus.OK);
	}

	// Redis Cafe Location info created
	// TODO 접속자 주소 or 경도/위도 기반으로 주위 리스트 조회
	@GetMapping("/created/cafeLocation")
	public void createdLocationInfo(@LongLatLocation RequestLocation location) {
		// 선택 위치 2000m 주변 카페 카테고리 리스트 Redis 등록
		cafeAroundMeService.createCafeLocationInfo(location);
	}

	// Location info get
	@GetMapping("findByCafeAroundMe")
	public ResponseEntity<List<GeoRadiusResponse>> findByCafeAroundMe(@LongLatLocation RequestLocation location) {
		List<GeoRadiusResponse> cafeLocation = cafeAroundMeService.findByCafeAroundMe(location);
		return new ResponseEntity<>(cafeLocation, HttpStatus.OK);
	}

}
