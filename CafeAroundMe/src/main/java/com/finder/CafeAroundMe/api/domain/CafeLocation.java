package com.finder.CafeAroundMe.api.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CafeLocation {

	@JsonProperty("documents")
	private List<Location> Locations;
	private Map<String, Object> meta;
}

@Data
class Location {
	private String placeName;
	private Double x;
	private Double y;
}
