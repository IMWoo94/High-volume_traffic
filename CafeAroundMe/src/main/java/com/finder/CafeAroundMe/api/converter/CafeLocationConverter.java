package com.finder.CafeAroundMe.api.converter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finder.CafeAroundMe.api.domain.CafeLocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CafeLocationConverter {

	private final ObjectMapper objectMapper;

	public CafeLocation toEntity(String jsonString) {
		/**
		 * Json 형식 String -> Object 변환 방식
		 * 1. JSONObject
		 * 2. ObjectMapper
		 */

		// 1. String 타입의 Json 형식 body 결과값 Json Object 로 변환
		// JSONObject jsonObject = new JSONObject(jsonString);
		// JSONObject meta = jsonObject.getJSONObject("meta");
		// JSONArray documents = jsonObject.getJSONArray("documents");
		// log.info("JSONObject : {}", jsonObject.toString());
		// log.info("jsonObject meta : {}", meta.toString());
		// log.info("jsonObject documents : {}", documents.toString());

		// API 요청에 담긴 모든 파라미터를 사용하지 않는 경우 불필요 파라미터 내용에 대해서 무시
		// objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return objectMapper.readValue(jsonString, CafeLocation.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
