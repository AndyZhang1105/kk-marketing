package com.kk.marketing.coupon.remote;

import java.util.List;
import java.util.Map;

/**
 * @author Zal
 */
public interface CouponDataRemote {

    /**
     * get stock map for coupon id list
     */
    Map<Long, Integer> getCouponStockMap(Long tenantId, List<Long> couponIdList);

}
