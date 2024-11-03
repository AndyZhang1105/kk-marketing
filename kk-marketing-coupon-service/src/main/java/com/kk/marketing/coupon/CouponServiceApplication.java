package com.kk.marketing.coupon;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class CouponServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(CouponServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CouponServiceApplication.class, args);
    }

}
