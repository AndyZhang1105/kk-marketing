package com.kk.marketing.coupon.aop;

import com.kk.arch.common.util.AspectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * 分布式锁注解切面
 *
 * @author Zal
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class DistributedLockAspect {

    private static final String REMOVE_IF_VAL = "if (redis.call('GET', KEYS[1]) == ARGV[1]) then return redis.call('DEL', KEYS[1]) else return 0 end";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 拦截被@DistributedLock注解的方法
     */
    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        // 根据注解参数生成redis key
        String key = generateKey(joinPoint, distributedLock);
        // 当前时间戳
        long currentTimestamp = System.currentTimeMillis();
        // 获取锁
        if (acquireLock(distributedLock, key, currentTimestamp)) {
            try {
                return joinPoint.proceed();
            } finally {
                // 方法执行完成是否需要立即释放锁
                Optional.ofNullable(distributedLock.isRelease() ? "NOT_NULL" : null).ifPresent(o -> this.unlock(key, currentTimestamp));
            }
        } else {
            // 获取锁失败
            throw new RuntimeException("获取" + key + "锁失败");
        }
    }

    /**
     * 根据注解参数生成redis key
     */
    private String generateKey(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        String key = AspectUtils.buildTemplate(joinPoint, distributedLock.key(), distributedLock.args());
        return Optional.ofNullable(key).orElseThrow(() -> new RuntimeException("缓存key不能为空"));
    }

    /**
     * 获取锁
     */
    private Boolean acquireLock(DistributedLock distributedLock, String key, long currentTimestamp) throws InterruptedException {
        // 锁过期时间（ms）
        long expire = distributedLock.expire();
        // 尝试获取锁的等待时间（ms）
        long timeout = distributedLock.timeout();
        // 尝试获取锁
        boolean tryLock = tryLock(key, currentTimestamp, expire);
        // 如果获取锁失败,且超时等待时间大于0，则睡眠distributedLock.tryInterval() ms后重试
        while (!tryLock && timeout > 0) {
            // 如果超过timeout时间还没获取到锁，则获取锁失败
            if (System.currentTimeMillis() - currentTimestamp > timeout) {
                break;
            }
            sleep(distributedLock.tryInterval());
            // 在次尝试获取锁
            tryLock = tryLock(key, currentTimestamp, expire);
        }
        return tryLock;
    }

    /**
     * 尝试获取锁
     */
    private boolean tryLock(String key, long currentTimestamp, long expire) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, currentTimestamp, expire, TimeUnit.MILLISECONDS));
    }

    /**
     * 释放锁, 释放时校验value值是否是当前线程的时间戳, 防止因为锁过期误删除其它线程的锁
     */
    private void unlock(String key, long currentTimestamp) {
        // 释放锁, 释放时校验value值是否是当前线程的时间戳, 防止因为锁过期误删除其它线程的锁
        RedisScript<Boolean> redisScript = RedisScript.of(REMOVE_IF_VAL, Boolean.class);
        this.redisTemplate.execute(redisScript, Collections.singletonList(key), currentTimestamp);
    }

}
