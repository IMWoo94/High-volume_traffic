package com.finder.CafeAroundMe.api.controller;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finder.CafeAroundMe.api.domain.CafeLocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@RestController
@RequestMapping("/openapi/cam")
@Slf4j
@RequiredArgsConstructor
public class CafeAroundMeController {

	private final StringRedisTemplate stringRedisTemplate;
	private final Jedis jedis;
	private final ObjectMapper objectMapper;

	// Redis key info get
	@GetMapping("/keys")
	public Set<String> getAllKeys() {
		Set<String> keys = jedis.keys("*");
		return keys;
	}

	// Redis Cafe Location info add
	@GetMapping("/location/add")
	public void getLocationInfoAdd() {
		// http + RESTful API template
		RestTemplate apiRequest = new RestTemplate();

		// header set
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "KakaoAK 416b63495de71dc7940f60aa2bafdc57");

		HttpEntity httpEntity = new HttpEntity(httpHeaders);

		// category_group_code=CE7 [ 카페 ]
		// 2000m 주변 카페 카테고리 리스트 API 호출
		ResponseEntity<String> result = apiRequest.exchange(
			"https://dapi.kakao.com/v2/local/search/category.json?category_group_code=CE7&radius=2000", HttpMethod.GET,
			httpEntity, String.class);

		/**
		 * Json 형식 String -> Object 변환 방식
		 * 1. JSONObject
		 * 2. ObjectMapper
		 */

		// 1. String 타입의 Json 형식 body 결과값 Json Object 로 변환
		JSONObject jsonObject = new JSONObject(result.getBody());
		JSONObject meta = jsonObject.getJSONObject("meta");
		JSONArray documents = jsonObject.getJSONArray("documents");
		log.info("JSONObject : {}", jsonObject.toString());
		log.info("jsonObject meta : {}", meta.toString());
		log.info("jsonObject documents : {}", documents.toString());

		// API 요청에 담긴 모든 파라미터를 사용하지 않는 경우 불필요 파라미터 내용에 대해서 무시
		// objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			CafeLocation cafeLocation = objectMapper.readValue(result.getBody(), CafeLocation.class);
			log.info("ObjectMapper read : {}", cafeLocation);

			String key = "cafeLocation";
			Pipeline pipelined = jedis.pipelined();
			cafeLocation.getLocations().forEach(it -> {
				log.info("locations :{}", it);
				pipelined.geoadd(key, it.getX(), it.getY(), it.getPlaceName());
			});
			pipelined.sync();
			
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
