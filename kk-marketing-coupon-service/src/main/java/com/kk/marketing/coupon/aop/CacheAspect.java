package com.kk.marketing.coupon.aop;

import com.kk.marketing.coupon.conf.RedisHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Zal
 */
@Component
@Aspect
public class CacheAspect {

    @Autowired
    private RedisHelper redisHelper;

    @Around("@annotation(cache)")
    public Object cacheMethod(ProceedingJoinPoint joinPoint, Cache cache) throws Throwable {
        String key = cache.key();
        if (key.isEmpty()) {
            key = generateKey(joinPoint);
        }

        // 尝试从缓存中获取数据
        Object result = redisHelper.get(key);
        if (result != null) {
            return result; // 如果缓存中有数据，直接返回
        }

        // 如果缓存中没有数据，则执行方法
        result = joinPoint.proceed();

        // 将结果存入缓存
        redisHelper.set(key, result, cache.ttl());

        return result;
    }

    private String generateKey(ProceedingJoinPoint joinPoint) {
        // 生成默认的缓存键
        // 这里可以根据实际需求自定义生成策略
        return joinPoint.getSignature().toShortString() + "_" + Arrays.toString(joinPoint.getArgs());
    }

}