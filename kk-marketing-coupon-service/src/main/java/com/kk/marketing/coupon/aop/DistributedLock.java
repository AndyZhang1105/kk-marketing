package com.kk.marketing.coupon.aop;

import java.lang.annotation.*;

/**
 * 分布式锁注解
 * @author 敖癸
 * @since 2024/3/4
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * redis锁 key名称,支持字符串模板：如：KfptAppName:Lock:{}, {}表示占位符, 由args中的参数替换
     */
    String key();

    /**
     * 模板参数,支持SpEL表达式, ex: {"#serialNo", "#serialNo.length()", "T(java.util.Random).nextInt()", "isTure?'success':'false'", "normal"}
     */
    String[] args() default {};

    /**
     * 锁过期时间（毫秒）,默认3秒
     */
    long expire() default 3000;

    /**
     * 尝试获取锁超时时间
     */
    long timeout() default 0;

    /**
     * 尝试获取锁间隔时间(ms)
     */
    int tryInterval() default 5;

    /**
     * 方法执行完是否立刻释放锁？默认true,表示方法执行完毕立即释放，false表示等到expire过期后释放
     */
    boolean isRelease() default true;
}
