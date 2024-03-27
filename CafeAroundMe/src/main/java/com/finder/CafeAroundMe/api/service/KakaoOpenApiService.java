package com.finder.CafeAroundMe.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.finder.CafeAroundMe.api.converter.CafeLocationConverter;
import com.finder.CafeAroundMe.api.domain.CafeLocation;
import com.finder.CafeAroundMe.api.domain.RequestLocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOpenApiService {

	@Value("${kakao.open-api.location.url}")
	private String PREFIX_URL;
	@Value("${kakao.open-api.location.rest-api}")
	private String REST_API;
	private final CafeLocationConverter cafeLocationConverter;

	public CafeLocation getCafeLocationInfo(RequestLocation location, int size, int page) {
		// http + RESTful API template
		RestTemplate apiRequest = new RestTemplate();

		// header set
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.add(HttpHeaders.AUTHORIZATION, REST_API);

		HttpEntity httpEntity = new HttpEntity(httpHeaders);

		StringBuilder sb = new StringBuilder(PREFIX_URL);
		sb.append("?")
			.append("category_group_code=CE7")
			.append("&x=").append(location.getLongitude())
			.append("&y=").append(location.getLatitude())
			.append("&radius=2000")
			.append("&page=").append(page)
			.append("&sort=distance");

		String url = sb.toString();

		// category_group_code=CE7 [ 카페 ]
		// 2000m 주변 카페 카테고리 리스트 API 호출
		ResponseEntity<String> result = apiRequest.exchange(
			url,
			HttpMethod.GET,
			httpEntity,
			String.class);

		return cafeLocationConverter.toEntity(result.getBody());
	}
}
