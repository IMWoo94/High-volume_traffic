package com.finder.CafeAroundMe.api.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.finder.CafeAroundMe.api.domain.CafeLocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.args.SortingOrder;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class CafeAroundMeService {

	private final Jedis jedis;
	private final KakaoOpenApiService kakaoOpenApiService;

	public Set<String> findAllKeysAndPattern(String pattern) {
		return jedis.keys(pattern);
	}

	public void createCafeLocationInfo() {
		CafeLocation cafeLocation;
		int page = 1;
		String key = "cafeLocation";
		Pipeline pipelined = jedis.pipelined();
		do {
			cafeLocation = findByCafeLocation(page++);
			cafeLocation.getLocations().forEach(it -> {
				log.info("locations :{}", it);
				pipelined.geoadd(key, it.getX(), it.getY(), it.getPlaceName());
			});
			pipelined.sync();
		} while (!cafeLocation.isEnd());
	}

	private CafeLocation findByCafeLocation(int page) {
		// category_group_code=CE7 [ 카페 ]
		// 2000m 주변 카페 카테고리 리스트 API 호출
		return kakaoOpenApiService.getCafeLocationInfo(15, page);
	}

	public List<GeoRadiusResponse> findByCafeAroundMe(
		double longitude,
		double latitude,
		double radius,
		GeoUnit unit
	) {
		GeoSearchParam geoSearchParam = new GeoSearchParam()
			.fromLonLat(new GeoCoordinate(longitude, latitude))
			.byRadius(radius, unit)
			.sortingOrder(SortingOrder.ASC);
		return jedis.geosearch("cafeLocation", geoSearchParam);
	}

}
