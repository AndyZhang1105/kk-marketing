package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.marketing.coupon.entity.CouponData;

import java.util.List;
import java.util.Map;

/**
 * @author Zal
 */
public interface CouponDataService extends IService<CouponData> {

    /**
     * get the num distributed of coupon
     */
    Map<Long, CouponData> getCouponDataMap(Long tenantId, List<Long> couponIdList);

    /**
     * get the num distributed of coupon
     */
    int getNumberDistributed(Long tenantId, Long couponId);

    /**
     * increase number distributed of coupon
     */
    boolean increaseNumberDistributed(Long tenantId, Long couponId, Integer num);

    /**
     * increase number consumed of coupon
     */
    boolean increaseNumberConsumed(Long tenantId, Long couponId, Integer num);

}
