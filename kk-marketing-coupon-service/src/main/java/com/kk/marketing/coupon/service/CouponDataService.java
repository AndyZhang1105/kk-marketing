package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.marketing.coupon.entity.CouponData;

/**
 * @author Zal
 */
public interface CouponDataService extends IService<CouponData> {

    int getCouponStock(Long tenantId, Long couponId);

    boolean deductStock(Long tenantId, Long couponId, Integer num);
}
