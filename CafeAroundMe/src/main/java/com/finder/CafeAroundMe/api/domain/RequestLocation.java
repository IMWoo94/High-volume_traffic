package com.finder.CafeAroundMe.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestLocation {

	private String longitude;
	private String latitude;
}
