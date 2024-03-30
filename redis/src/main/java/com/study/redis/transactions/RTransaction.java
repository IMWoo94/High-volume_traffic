package com.study.redis.transactions;

import jakarta.annotation.PostConstruct;

public interface RTransaction {

	void transaction();

	void reset();

	@PostConstruct
	default void init() {
		transaction();

		reset();
	}
}
