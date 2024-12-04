package com.kk.marketing.coupon.mq;

import com.kk.marketing.coupon.conf.TenantContextHolder;
import com.kk.marketing.coupon.remote.CouponDistributionRemote;
import com.kk.marketing.coupon.req.CouponDistributionReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;

import static com.kk.arch.common.constants.CommonConstants.TENANT_ID;

/**
 * @author Zal
 */
@Component
@Slf4j
public class MqConsumer {

    @Autowired
    private CouponDistributionRemote couponDistributionRemote;

    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

    @Bean
    public Consumer<GenericMessage<byte[]>> broadcastConsumer() {
        return msg -> {
            final Object object = genericJackson2JsonRedisSerializer.deserialize(msg.getPayload());
            System.out.println("Regular Consumer Received from Topic 2: " + object);
        };
    }

    @Bean
    public Consumer<GenericMessage<byte[]>> squareConsumer() {
        return msg -> {
            final Object object = genericJackson2JsonRedisSerializer.deserialize(msg.getPayload());
            System.out.println("Square Consumer Received from Topic 1: " + object);
        };
    }

    @Bean
    public Consumer<GenericMessage<byte[]>> asyncDistributeCouponConsumer() {
        return msg -> {
            try {
                TenantContextHolder.setTenantId(Long.valueOf(Objects.requireNonNull(msg.getHeaders().get(TENANT_ID)).toString()));
                final Object object = genericJackson2JsonRedisSerializer.deserialize(msg.getPayload());
                log.info("收到新的异步发券消息通知：{}", msg);
                couponDistributionRemote.sendCouponUserForMq((CouponDistributionReqDto) object);
            } catch (Exception e) {
                log.error("ERROR: asyncDistributeCouponConsumer, msg: {}, exception: {}, stack: {}", msg, e.getMessage(), e.getStackTrace());
            }
        };
    }

}
