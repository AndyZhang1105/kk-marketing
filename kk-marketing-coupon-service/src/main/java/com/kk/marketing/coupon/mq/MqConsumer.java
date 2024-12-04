package com.kk.marketing.coupon.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

/**
 * @author Zal
 */
@Configuration
@Slf4j
public class MqConsumer {

    @Bean
    public Consumer<Message<String>> consumer() {
        return msg -> {
            log.info(Thread.currentThread().getName() + " Consumer Receive New Messages: " + msg.getPayload());
        };
    }

}
