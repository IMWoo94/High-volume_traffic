package com.finder.CafeAroundMe.api.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finder.CafeAroundMe.api.service.CafeAroundMeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/openapi/cam")
@Slf4j
@RequiredArgsConstructor
public class CafeAroundMeController {

	private final CafeAroundMeService cafeAroundMeService;

	// Redis key info get
	@GetMapping("/findAllKeys")
	public ResponseEntity<Set<String>> getAllKeys() {
		Set<String> keys = cafeAroundMeService.findAllKeysAndPattern("*");
		return new ResponseEntity<>(keys, HttpStatus.OK);
	}

	// Redis Cafe Location info add
	@GetMapping("/created/cafeLocation")
	public void createdLocationInfo() {
		// 2000m 주변 카페 카테고리 리스트 Redis 등록
		cafeAroundMeService.createCafeLocationInfo();
	}

}
