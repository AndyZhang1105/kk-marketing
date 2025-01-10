package com.kk.marketing.coupon.conf;

import com.kk.arch.dubbo.common.conf.ApplicationContextHelper;
import com.kk.arch.dubbo.common.conf.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.kk.arch.dubbo.common.constant.CommonConstants.TENANT_ID;

/**
 * @author Zal
 */
@Component
@Slf4j
public class MqHelper {

    public static void sendMsg(String bindingName, Object object) {
        if(Objects.isNull(object)) {
            return;
        }

        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        byte[] bytes = genericJackson2JsonRedisSerializer.serialize(object);

        Map<String, Object> headers = new HashMap<>();
        headers.put(MessageConst.PROPERTY_KEYS, UUID.randomUUID().toString());
        headers.put(TENANT_ID, TenantContextHolder.getTenantId());

        Message<byte[]> msg = new GenericMessage<byte[]>(Objects.requireNonNull(bytes), headers);
        Objects.requireNonNull(ApplicationContextHelper.getBean(StreamBridge.class)).send(bindingName, msg);
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
                Objects.requireNonNull(ApplicationContextHelper.getBean(StreamBridge.class)).send("producer-out-0", msg);
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
                Objects.requireNonNull(ApplicationContextHelper.getBean(StreamBridge.class)).send("squareProducer-out-0", msg);
            }
        };
    }


}
