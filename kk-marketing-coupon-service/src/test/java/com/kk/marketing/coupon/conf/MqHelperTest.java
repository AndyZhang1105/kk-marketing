package com.kk.marketing.coupon.conf;

import com.kk.marketing.coupon.vo.CouponVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.kk.marketing.coupon.conf.MqHelper.BINDING_NAME_BROADCAST;
import static com.kk.marketing.coupon.conf.MqHelper.BINDING_NAME_SQUARE;

@SpringBootTest()
@ActiveProfiles("dev")
@Slf4j
public class MqHelperTest {

    @Autowired
    private MqHelper mqHelper;

    @Test
    public void sendMsg() {
        mqHelper.sendMsg(BINDING_NAME_BROADCAST, CouponVo.builder().id(RandomUtils.nextLong()).build());
        mqHelper.sendMsg(BINDING_NAME_SQUARE, CouponVo.builder().id(RandomUtils.nextLong()).build());
        mqHelper.sendMsg(BINDING_NAME_BROADCAST, CouponVo.builder().id(RandomUtils.nextLong()).build());
        mqHelper.sendMsg(BINDING_NAME_SQUARE, CouponVo.builder().id(RandomUtils.nextLong()).build());
    }

    @Test
    public void sendMsgTest() {
        mqHelper.producer();
        mqHelper.producer();
        mqHelper.producer();
        mqHelper.producer();
    }

}
