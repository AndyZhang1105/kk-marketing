package com.kk.marketing.coupon.conf;

import com.kk.marketing.coupon.vo.CouponVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static com.kk.marketing.coupon.constant.BusinessConstants.BINDING_NAME_BROADCAST;
import static com.kk.marketing.coupon.constant.BusinessConstants.BINDING_NAME_SQUARE;

@SpringBootTest()
@ActiveProfiles("dev")
@Slf4j
public class MqHelperTest {

    @Test
    public void sendMsg() {
        MqHelper.sendMsg(BINDING_NAME_BROADCAST, CouponVo.builder().id(RandomUtils.nextLong()).build());
        MqHelper.sendMsg(BINDING_NAME_SQUARE, CouponVo.builder().id(RandomUtils.nextLong()).build());
        MqHelper.sendMsg(BINDING_NAME_BROADCAST, CouponVo.builder().id(RandomUtils.nextLong()).build());
        MqHelper.sendMsg(BINDING_NAME_SQUARE, CouponVo.builder().id(RandomUtils.nextLong()).build());
    }

    @Test
    public void sendMsgTest() {
        Objects.requireNonNull(ApplicationContextHelper.getBean(MqHelper.class)).producer();
        Objects.requireNonNull(ApplicationContextHelper.getBean(MqHelper.class)).producer();
        Objects.requireNonNull(ApplicationContextHelper.getBean(MqHelper.class)).producer();
        Objects.requireNonNull(ApplicationContextHelper.getBean(MqHelper.class)).producer();
    }

}
