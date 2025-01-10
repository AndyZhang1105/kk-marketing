package com.kk.marketing.coupon.algorithm;

import org.assertj.core.util.Lists;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FullMinusCouponDpAlgorithmTest {

    public static void main(String[] args) {
        List<FullMinusCouponDpAlgorithm.FullMinusCoupon> coupons = Lists.newArrayList();
        coupons.add(new FullMinusCouponDpAlgorithm.FullMinusCoupon("f0", 5, 10));  // 满10减5
        coupons.add(new FullMinusCouponDpAlgorithm.FullMinusCoupon("t0", 10, 20)); // 满20减10
        coupons.add(new FullMinusCouponDpAlgorithm.FullMinusCoupon("ff", 15, 30));  // 满30减15

        for (int i = 0; i < 200; i++) {
            coupons.add(new FullMinusCouponDpAlgorithm.FullMinusCoupon(UUID.randomUUID().toString(), i + 5, i + 50));
        }
        int totalAmount = 999999;

        Map.Entry<Integer, List<FullMinusCouponDpAlgorithm.FullMinusCoupon>> maxDiscountCombination = FullMinusCouponDpAlgorithm.findMaxDiscountCombination(coupons, totalAmount);
        System.out.println("最大优惠金额: " + maxDiscountCombination.getKey());
        System.out.println("对应的券组合:");
        maxDiscountCombination.getValue().forEach(System.out::println);
    }

}
