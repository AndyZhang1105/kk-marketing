package com.kk.marketing.coupon.aop;

import java.lang.annotation.*;

/**
 * @author Zal
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LocalCache {

    String key() default ""; // 缓存键

    long ttl() default 60; // 默认缓存时间60秒

}