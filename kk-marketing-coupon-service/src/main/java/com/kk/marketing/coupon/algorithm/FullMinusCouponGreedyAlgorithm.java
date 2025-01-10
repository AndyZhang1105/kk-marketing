package com.kk.marketing.coupon.algorithm;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Zal
 */
public class FullMinusCouponGreedyAlgorithm {

    public static class FullMinusCoupon implements Comparable<FullMinusCouponDpAlgorithm.FullMinusCoupon> {
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
        public int compareTo(FullMinusCouponDpAlgorithm.FullMinusCoupon other) {
            // 根据具体需求调整比较逻辑，这里假设按门槛值升序排列
            return Integer.compare(this.threshold, other.threshold);
        }
    }

    /**
     * 使用深度优先搜索（DFS）加回溯寻找最大优惠金额及其对应的券组合。
     *
     * @param coupons   排序后的优惠券列表
     * @param totalAmount 订单总金额
     * @return 最大优惠金额以及对应的券组合列表
     */
    public static Map.Entry<Integer, List<FullMinusCoupon>> findMaxDiscountCombination(List<FullMinusCoupon> coupons, int totalAmount) {
        List<FullMinusCoupon> currentCombination = new ArrayList<>();
        List<FullMinusCoupon> bestCombination = new ArrayList<>();
        int[] maxDiscount = {0}; // 使用数组来允许方法内部修改其值

        // 尝试所有可能的优惠券组合
        backtrack(coupons, totalAmount, 0, currentCombination, bestCombination, maxDiscount);

        return new AbstractMap.SimpleEntry<>(maxDiscount[0], bestCombination);
    }

    private static void backtrack(List<FullMinusCoupon> coupons, int remainingAmount, int startIndex, List<FullMinusCoupon> currentCombination, List<FullMinusCoupon> bestCombination, int[] maxDiscount) {
        if (remainingAmount < 0) {
            return; // 如果剩余金额为负数，则停止当前路径
        }

        int currentDiscount = currentCombination.stream().mapToInt(c -> c.discount).sum();
        if (currentDiscount > maxDiscount[0]) {
            // 更新最优解
            maxDiscount[0] = currentDiscount;
            bestCombination.clear();
            bestCombination.addAll(currentCombination);
        }

        for (int i = startIndex; i < coupons.size(); i++) {
            FullMinusCoupon coupon = coupons.get(i);
            if (remainingAmount >= coupon.threshold) {
                // 尝试使用这张优惠券
                currentCombination.add(coupon);
                backtrack(coupons, remainingAmount - coupon.threshold, i + 1, currentCombination, bestCombination, maxDiscount);
                // 回溯，移除刚刚添加的优惠券
                currentCombination.remove(currentCombination.size() - 1);
            }
        }
    }

}
