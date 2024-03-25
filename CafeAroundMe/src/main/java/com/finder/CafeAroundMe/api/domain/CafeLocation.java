package com.finder.CafeAroundMe.api.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CafeLocation {

	@JsonProperty("documents")
	private List<Location> Locations;
	private Map<String, Object> meta;
}


