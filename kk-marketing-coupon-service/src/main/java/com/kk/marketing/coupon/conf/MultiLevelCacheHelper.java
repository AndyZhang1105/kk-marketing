package com.kk.marketing.coupon.conf;

import com.kk.arch.common.util.JsonUtils;
import com.kk.arch.common.util.Md5Utils;
import com.kk.arch.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import static java.lang.Thread.sleep;

/**
 * @author Zal
 */
@Component
@Slf4j
public class MultiLevelCacheHelper {

    /**
     * 线程自旋的最大次数，默认30次
     */
    private final static int SPIN_MAX_TIMES = 30;

    /**
     * 每次自旋的时长，100毫秒
     */
    private final static int SPIN_MAX_DURATION = 100;

    private final static OhcHelper OHC_HELPER = ApplicationContextHelper.getBean(OhcHelper.class);

    private final static RedisHelper REDIS_HELPER = ApplicationContextHelper.getBean(RedisHelper.class);

    /**
     * 多级缓存的缓存值获取
     * @param key 键
     * @param ttl 单位是毫秒, 默认缓存时间60秒
     * @return 缓存的值
     */
    public static Object get(String key, long ttl, Supplier<Object> supplier)  {
        try {
            return getCache(key, ttl, supplier);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
        }
        return null;
    }

    protected static Object getCache(String key, long ttl, Supplier<Object> supplier) throws InterruptedException {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("缓存的Key不能为空");
        }

        Object result = null;

        for(int i = 0; i < SPIN_MAX_TIMES; i++) {
            // 从本地缓存获取数据
            Object localCacheObject = OHC_HELPER.get(key);
            if (!Objects.isNull(localCacheObject)) {
                log.info("命中本地缓存：{}", key);
                result = localCacheObject;
                break;
            }

            // 如果本地缓存中没有，则获取锁，再调用实际方法
            final String localLockKey = "LOCAL_LOCK_" + key;
            final String localLockValue = UUID.randomUUID().toString();
            if(REDIS_HELPER.tryLock(localLockKey, localLockValue, ttl)) {
                // 再次检查本地缓存，防止其他线程已经设置了缓存
                localCacheObject = OHC_HELPER.get(key);
                if (!Objects.isNull(localCacheObject)) {
                    log.info("命中本地缓存：{}", key);
                    result = localCacheObject;
                } else {
                    // 从分布式缓存获取数据
                    Object redisCacheObject = REDIS_HELPER.get(key);
                    if (!Objects.isNull(redisCacheObject)) {
                        log.info("命中分布式缓存：{}", key);
                        result = redisCacheObject;
                        OHC_HELPER.put(key, result, ttl);
                    } else {
                        // 如果本地缓存及分布式缓存中都没有，则获取锁，再调用实际方法
                        final String redisLockKey = "REDIS_LOCK_" + key;
                        final String redisLockValue = UUID.randomUUID().toString();
                        if (REDIS_HELPER.tryLock(redisLockKey, redisLockValue, ttl)) {
                            // 再次检查分布式缓存，防止其他线程已经设置了缓存
                            redisCacheObject = REDIS_HELPER.get(key);
                            if(!Objects.isNull(redisCacheObject)) {
                                log.info("命中分布式缓存：{}", key);
                                result = redisCacheObject;
                            } else {
                                log.info("未命中且执行方法：{}", key);
                                result = supplier.get();
                                REDIS_HELPER.set(key, result, ttl);
                            }
                            OHC_HELPER.put(key, result, ttl);
                            REDIS_HELPER.unlock(redisLockKey, redisLockValue);
                        } else {
                            // 线程自旋，过100毫秒再查
                            sleep(SPIN_MAX_DURATION);
                            log.info("分布式缓存自旋:" + i);
                        }
                    }
                }
                REDIS_HELPER.unlock(localLockKey, localLockValue);
            } else {
                // 线程自旋，过100毫秒再查
                log.info("本地缓存自旋:" + i);
                sleep(SPIN_MAX_DURATION);
            }

            if(!Objects.isNull(result)) {
                break;
            }
        }

        if(Objects.isNull(result)) {
            throw new RuntimeException("系统繁忙，请稍后再试");
        }

        return result;
    }

    public static String generateKey(Object params) {
        // 生成唯一的缓存键
        // return joinPoint.getSignature().toShortString() + Arrays.toString(joinPoint.getArgs());
        return TenantContextHolder.getTenantId() + "_" + Md5Utils.md5(JsonUtils.toJsonString(params));
    }

}
