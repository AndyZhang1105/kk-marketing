package com.kk.marketing.coupon.aop;

import java.lang.annotation.*;

/**
 * @author Zal
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiLevelCache {

    String key() default "";

    /**
     * 单位是毫秒, 默认缓存时间60秒
     */
    long ttl() default 60 * 1000;

}