package com.kk.marketing.coupon.conf;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("dev")
@Slf4j
public class RocketMqHelperTest {

    @Autowired
    private RocketMqHelper rocketMqHelper;

    @Test
    public void sendMsgTest() {
        rocketMqHelper.producer();
        rocketMqHelper.producer();
        rocketMqHelper.producer();
        rocketMqHelper.producer();
    }

}
