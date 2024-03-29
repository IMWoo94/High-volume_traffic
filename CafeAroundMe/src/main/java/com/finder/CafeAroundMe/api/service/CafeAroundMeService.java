package com.finder.CafeAroundMe.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.finder.CafeAroundMe.api.common.annotation.DistributedLock;
import com.finder.CafeAroundMe.api.domain.CafeLocation;
import com.finder.CafeAroundMe.api.domain.RequestLocation;

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
	private final RedissonClient redissonClient;

	@DistributedLock(key = "redisLock")
	public Set<String> findAllKeysAndPattern(String pattern) {
		return jedis.keys(pattern);
	}

	public Set<String> findAllRedissonKeysAndPattern(String pattern) {
		// Redisson 을 이용한 분산락 기법의 key 조회
		Set<String> result = new HashSet<>();

		RKeys keys = redissonClient.getKeys();
		Iterable<String> iter = keys.getKeys();
		iter.forEach(result::add);

		return result;
	}

	public void createCafeLocationInfo(RequestLocation location) {
		CafeLocation cafeLocation;
		int page = 1;
		String key = "cafeLocation";
		beforeCreatedLocation(key);

		Pipeline pipelined = jedis.pipelined();
		do {
			cafeLocation = findByCafeLocation(location, page++);
			cafeLocation.getLocations().forEach(it -> {
				log.info("locations :{}", it);
				pipelined.geoadd(key, it.getX(), it.getY(), it.getPlaceName());
			});
			pipelined.sync();
		} while (!cafeLocation.isEnd());
	}

	private CafeLocation findByCafeLocation(RequestLocation location, int page) {
		// category_group_code=CE7 [ 카페 ]
		// 2000m 주변 카페 카테고리 리스트 API 호출
		return kakaoOpenApiService.getCafeLocationInfo(location, 15, page);
	}

	private void beforeCreatedLocation(String key) {
		// 기존의 등록된 내용 제거
		jedis.unlink(key);
	}

	public List<GeoRadiusResponse> findByCafeAroundMe(
		RequestLocation location
	) {
		GeoSearchParam geoSearchParam = new GeoSearchParam()
			.fromLonLat(new GeoCoordinate(Double.parseDouble(location.getLongitude()),
				Double.parseDouble(location.getLatitude())))
			.byRadius(2000, GeoUnit.M)
			.withCoord()
			.withDist()
			.sortingOrder(SortingOrder.ASC);
		return jedis.geosearch("cafeLocation", geoSearchParam);
	}

}
