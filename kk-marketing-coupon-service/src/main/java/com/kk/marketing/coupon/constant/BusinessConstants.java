package com.kk.marketing.coupon.constant;

/**
 * @author Zal
 */
public class BusinessConstants {

    /**
     * Spring Cloud Stream通道常量
     */
    public static String BINDING_NAME_BROADCAST = "producer-out-0";
    public static String BINDING_NAME_SQUARE = "squareProducer-out-0";
    public static String BINDING_NAME_ASYNC_DISTRIBUTE_COUPON = "asyncDistributeCouponProducer-out-0";

}
