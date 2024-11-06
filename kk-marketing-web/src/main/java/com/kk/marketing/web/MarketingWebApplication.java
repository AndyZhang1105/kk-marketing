package com.kk.marketing.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class MarketingWebApplication {

    private static final Logger log = LoggerFactory.getLogger(MarketingWebApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MarketingWebApplication.class, args);
    }

}
