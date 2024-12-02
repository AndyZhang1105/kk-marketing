package com.kk.marketing.coupon.conf;

import lombok.extern.slf4j.Slf4j;
import org.caffinitas.ohc.Eviction;
import org.caffinitas.ohc.OHCache;
import org.caffinitas.ohc.OHCacheBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Zal
 */
@Component
@Slf4j
public class OhcHelper {

    private static final OHCache<String, Object> OH_CACHE = OHCacheBuilder.<String, Object>newBuilder()
                                                                          .keySerializer(new FastJsonCacheSerializer<String>(String.class))
                                                                          .valueSerializer(new FastJsonCacheSerializer<Object>(Object.class))
                                                                          //.hashMode(HashAlgorithm.CRC32C)
                                                                          //单位是字节，默认64M空间
                                                                          .capacity(64 * 1024 * 1024L)
                                                                          .timeouts(true)
                                                                          .defaultTTLmillis(600 * 1000)
                                                                          .eviction(Eviction.LRU)
                                                                          .build();

    /**
     * 设置值
     *
     * @param k key
     * @param v value
     * @return b
     */
    public boolean put(String k, Object v) {
        return put(k, v, 9223372036854775807L);
    }

    /**
     * 设置缓存值
     * @param k 键
     * @param v 值
     * @param time 过期时间，单位是秒, -1表示永不过期
     * @return boolean
     */
    public boolean put(String k, Object v, Long time) {
        try {
            return OH_CACHE.put(k, v, time / 1000);
        } catch (Exception e) {
            log.error("ohc cache put error", e);
            return false;
        }
    }

    /**
     * 获取缓存值
     * @param k 键
     * @return 值
     */
    public Object get(String k) {
        return OH_CACHE.get(k);
    }

}
