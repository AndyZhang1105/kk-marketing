package com.kk.marketing.coupon.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author Zal
 */
@Component
@Slf4j
public class MqConsumer {

    @Bean
    public Consumer<String> broadcastConsumer() {
        return message -> System.out.println("Regular Consumer Received from Topic 2: " + message);
    }

    @Bean
    public Consumer<String> squareConsumer() {
        return message -> System.out.println("Square Consumer Received from Topic 1: " + message);
    }

}
