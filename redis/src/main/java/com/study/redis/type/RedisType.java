package com.study.redis.type;

import jakarta.annotation.PostConstruct;

public interface RedisType {

	public void run();

	@PostConstruct
	default void init() {
		run();
	}
}
