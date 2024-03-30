package com.study.redis.type;

public interface RedisType {

	public void run();

	public void reset();

	// @PostConstruct
	default void init() throws InterruptedException {
		run();

		// 1초 후 type reset 작업
		Thread.sleep(1000);
		reset();
	}
}
