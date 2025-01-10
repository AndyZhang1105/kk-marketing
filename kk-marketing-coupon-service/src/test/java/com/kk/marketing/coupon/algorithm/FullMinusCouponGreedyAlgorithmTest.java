package com.kk.marketing.coupon.algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FullMinusCouponGreedyAlgorithmTest {

    public static void main(String[] args) {
        List<FullMinusCouponGreedyAlgorithm.FullMinusCoupon> coupons = Arrays.asList(
                new FullMinusCouponGreedyAlgorithm.FullMinusCoupon("f0", 5, 10),  // 满10减5
                new FullMinusCouponGreedyAlgorithm.FullMinusCoupon("t0", 10, 20), // 满20减10
                new FullMinusCouponGreedyAlgorithm.FullMinusCoupon("ff", 15, 30)  // 满30减15
        );
        int totalAmount = 100;

        // 对优惠券按照门槛值进行排序（可选）
        coupons.sort((a, b) -> Integer.compare(a.threshold, b.threshold));

        Map.Entry<Integer, List<FullMinusCouponGreedyAlgorithm.FullMinusCoupon>> maxDiscountCombination = FullMinusCouponGreedyAlgorithm.findMaxDiscountCombination(coupons, totalAmount);
        System.out.println("最大优惠金额: " + maxDiscountCombination.getKey());
        System.out.println("对应的券组合:");
        maxDiscountCombination.getValue().forEach(System.out::println);
    }

}
