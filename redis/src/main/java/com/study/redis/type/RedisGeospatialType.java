package com.study.redis.type;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

@Slf4j
@RequiredArgsConstructor
public class RedisGeospatialType implements RedisType {

	private final Jedis jedis;

	@Override
	public void run() {

		// Geospatial prefix : GEO
		jedis.geoadd("stores:geo", 127.02985530619755, 37.49911212874, "place1");
		jedis.geoadd("stores:geo", 127.0333352287619, 37.491921163986234, "place2");

		List<GeoCoordinate> place1 = jedis.geopos("stores:geo", "place1");
		place1.forEach(it -> log.info("{} | {}", it.getLatitude(), it.getLongitude()));
		List<GeoCoordinate> place2 = jedis.geopos("stores:geo", "place2");
		place2.forEach(it -> log.info("{} | {}", it.getLatitude(), it.getLongitude()));

		// geo dist
		Double geodistM = jedis.geodist("stores:geo", "place1", "place2", GeoUnit.M);
		Double geodistKM = jedis.geodist("stores:geo", "place1", "place2", GeoUnit.KM);

		log.info("place1 place2 geodistM {}", geodistM);
		log.info("place1 place2 geodistKM {}", geodistKM);

		// geo search
		List<GeoRadiusResponse> geosearch = jedis.geosearch(
			"stores:geo",
			new GeoCoordinate(127.031, 37.495),
			500,
			GeoUnit.M
		);

		geosearch.forEach(response -> {
			log.info(response.getMemberByString());
		});

		// radius
		List<GeoRadiusResponse> georadius = jedis.georadius("stores:geo", 127.030, 37.495, 800.0, GeoUnit.M);
		georadius.forEach(it -> log.info(it.getMemberByString()));

		// GeoRadiusResponse member 이외의 필드 값을 가져오기 위한 방식으로 변경
		List<GeoRadiusResponse> geosearch1 = jedis.geosearch(
			"stores:geo",
			new GeoSearchParam()
				.fromLonLat(new GeoCoordinate(127.031, 37.495))
				.byRadius(500, GeoUnit.M)
				.withDist()
				.withCoord()
		);

		log.info("GeoSearchParam");
		geosearch1.forEach(response -> {
			log.info("member name {}", response.getMemberByString());
			log.info("distance {} ", response.getDistance());
			log.info("coordinate {}", response.getCoordinate().toString());
		});

	}

	@Override
	public void reset() {
		Set<String> keys = jedis.keys("*");
		Pipeline pipelined = jedis.pipelined();
		keys.forEach(pipelined::unlink);
		pipelined.syncAndReturnAll();
	}

}
