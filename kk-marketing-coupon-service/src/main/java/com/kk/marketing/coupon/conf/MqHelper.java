package com.kk.marketing.coupon.conf;

import com.kk.arch.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Zal
 */
@Component
@Slf4j
public class MqHelper {

    @Autowired
    private StreamBridge streamBridge;

    public static String BINDING_NAME_BROADCAST = "producer-out-0";
    public static String BINDING_NAME_SQUARE = "squareProducer-out-0";

    public void sendMsg(String bindingName, Object object) {
        final String jsonMsg = JsonUtils.toJsonString(object);

        Map<String, Object> headers = new HashMap<>();
        headers.put(MessageConst.PROPERTY_KEYS, UUID.randomUUID().toString());
        headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, TenantContextHolder.getTenantId());

        Message<String> msg = new GenericMessage<String>(jsonMsg, headers);
        streamBridge.send(bindingName, msg);
    }

    // @Bean
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

    // @Bean
    public ApplicationRunner squareProducer() {
        return args -> {
            for (int i = 0; i < 100; i++) {
                String key = "KEY" + i;
                Map<String, Object> headers = new HashMap<>();
                headers.put(MessageConst.PROPERTY_KEYS, key);
                headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, i);
                Message<String> msg = new GenericMessage<String>("Hello RocketMQ Square " + i, headers);
                streamBridge.send("squareProducer-out-0", msg);
            }
        };
    }


}
