package com.finder.CafeAroundMe.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
	private String placeName;
	private Double x;
	private Double y;
}