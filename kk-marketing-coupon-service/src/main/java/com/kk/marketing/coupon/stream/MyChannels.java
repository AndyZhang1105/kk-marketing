package com.kk.marketing.coupon.stream;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Zal
 */
public interface MyChannels {

    String INPUT = "input";
    String OUTPUT = "output";

    SubscribableChannel input();

    MessageChannel output();
}