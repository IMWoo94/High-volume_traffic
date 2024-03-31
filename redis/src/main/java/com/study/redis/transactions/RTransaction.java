package com.study.redis.transactions;

public interface RTransaction {

	void transaction();

	void reset();

	// @PostConstruct
	default void init() {
		transaction();

		reset();
	}
}
