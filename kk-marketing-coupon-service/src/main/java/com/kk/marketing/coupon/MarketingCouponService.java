package com.kk.marketing.coupon;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Zal
 */
@SpringBootApplication
@EnableDubbo
@EnableAspectJAutoProxy
public class MarketingCouponService {

    private static final Logger log = LoggerFactory.getLogger(MarketingCouponService.class);

    public static void main(String[] args) {
        SpringApplication.run(MarketingCouponService.class, args);
    }

}
