package com.kk.marketing.coupon.algorithm;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Zal
 * 满减券的动态规划算法，找出最优的券组合，此算法与01背包的解法是一样的
 */
public class FullMinusCouponDpAlgorithm {

    public static class FullMinusCoupon implements Comparable<FullMinusCoupon> {
        String couponCode; //券码
        int discount; // 优惠金额
        int threshold; // 满足此金额门槛才能使用这张优惠券

        public FullMinusCoupon(String couponCode, int discount, int threshold) {
            this.couponCode = couponCode;
            this.discount = discount;
            this.threshold = threshold;
        }

        @Override
        public String toString() {
            return "Coupon(couponCode=" + couponCode + ", discount=" + discount + ", threshold=" + threshold + ")";
        }

        @Override
        public int compareTo(FullMinusCoupon other) {
            // 根据具体需求调整比较逻辑，这里假设按门槛值升序排列
            return Integer.compare(this.threshold, other.threshold);
        }
    }

    /**
     * 使用动态规划寻找最大优惠金额及其对应的券组合。
     *
     * @param coupons     排序后的优惠券列表
     * @param totalAmount 订单总金额
     * @return 最大优惠金额以及对应的券组合列表
     */
    public static Map.Entry<Integer, List<FullMinusCoupon>> findMaxDiscountCombination(List<FullMinusCoupon> coupons, int totalAmount) {
        int n = coupons.size();
        int[][] dp = new int[n + 1][totalAmount + 1];

        // 填充dp表
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= totalAmount; j++) {
                dp[i][j] = dp[i - 1][j]; // 不选当前优惠券的情况
                if (j >= coupons.get(i - 1).threshold) {
                    // 考虑选择当前优惠券的情况
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - coupons.get(i - 1).threshold] + coupons.get(i - 1).discount);
                }
            }
        }

        // 回溯查找组成最大优惠金额的优惠券组合
        List<FullMinusCoupon> result = new ArrayList<>();
        for (int i = n, j = totalAmount; i > 0 && j > 0; i--) {
            if (dp[i][j] != dp[i - 1][j]) {
                // 如果选择了这张优惠券
                result.add(coupons.get(i - 1));
                j -= coupons.get(i - 1).threshold;
            }
        }

        // 返回最大优惠金额及其对应的券组合列表
        return new AbstractMap.SimpleEntry<>(dp[n][totalAmount], result);
    }

}



