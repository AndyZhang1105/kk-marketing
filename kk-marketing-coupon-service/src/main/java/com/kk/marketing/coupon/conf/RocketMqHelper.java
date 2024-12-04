package com.kk.marketing.coupon.conf;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zal
 */
@Component
@Slf4j
public class RocketMqHelper {

    @Autowired
    private StreamBridge streamBridge;

    public static String BINDING_NAME_BROADCAST = "produceToBroadcast-out-0";
    public static String BINDING_NAME_SQUARE = "produceToSquare-out-0";

    @Bean
    public ApplicationRunner producer() {
        return args -> {
            for (int i = 0; i < 100; i++) {
                String key = "KEY" + i;
                Map<String, Object> headers = new HashMap<>();
                headers.put(MessageConst.PROPERTY_KEYS, key);
                headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, i);
                Message<String> msg = new GenericMessage<String>("Hello RocketMQ " + i, headers);
                streamBridge.send("producer-out-0", msg);
            }
        };
    }

}
