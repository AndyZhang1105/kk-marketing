package com.kk.marketing.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Zal
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
@Slf4j
public class MarketingWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketingWebApplication.class, args);
    }

}
