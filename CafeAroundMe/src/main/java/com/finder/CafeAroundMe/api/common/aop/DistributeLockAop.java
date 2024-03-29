package com.finder.CafeAroundMe.api.common.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.finder.CafeAroundMe.api.common.annotation.DistributedLock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Aspect
public class DistributeLockAop {

	private static final String REDISSON_LOCK_PREFIX = "LOCK:";

	private final RedissonClient redissonClient;

	@Around("@annotation(com.finder.CafeAroundMe.api.common.annotation.DistributedLock)")
	public Object lock(final ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedLock distributeLock = method.getAnnotation(DistributedLock.class);

		String key = REDISSON_LOCK_PREFIX;
		RLock lock = redissonClient.getLock(key);

		try {
			boolean available = lock.tryLock(distributeLock.waitTime(), distributeLock.leaseTime(),
				distributeLock.timeUnit());
			if (!available) {
				log.info("lock available false");
				return false;
			}
			return joinPoint.proceed();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			try {
				lock.unlock();   // (4)
			} catch (IllegalMonitorStateException e) {
				log.info("Redisson Lock Already UnLock {} {}", method.getName(), key);
			}
		}
	}

}
