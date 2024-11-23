package com.kk.marketing.coupon;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Zal
 */
@SpringBootApplication
@EnableDubbo
@EnableDubboConfig
@EnableAspectJAutoProxy
@Slf4j
public class MarketingCouponService {

    public static void main(String[] args) {
        SpringApplication.run(MarketingCouponService.class, args);
    }

}
