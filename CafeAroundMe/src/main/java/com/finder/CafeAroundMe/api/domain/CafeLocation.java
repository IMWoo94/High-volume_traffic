package com.finder.CafeAroundMe.api.domain;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CafeLocation {

	private List<Document> documents;
	private Map<String, Object> meta;
}

@Data
class Document {
	private String placeName;
	private Double x;
	private Double y;
}
