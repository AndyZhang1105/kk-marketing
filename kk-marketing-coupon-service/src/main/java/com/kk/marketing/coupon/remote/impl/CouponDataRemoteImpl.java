package com.kk.marketing.coupon.remote.impl;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.entity.CouponData;
import com.kk.marketing.coupon.remote.CouponDataRemote;
import com.kk.marketing.coupon.service.CouponDataService;
import com.kk.marketing.coupon.service.CouponService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Zal
 */
@DubboService
public class CouponDataRemoteImpl implements CouponDataRemote {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponDataService couponDataService;

    @Override
    public Map<Long, Integer> getCouponStockMap(Long tenantId, List<Long> couponIdList) {
        final Map<Long, CouponData> couponDataMap = couponDataService.getCouponDataMap(couponIdList);
        final Map<Long, Coupon> couponMap = couponService.queryMap(couponIdList);

        Map<Long, Integer> resultMap = Maps.newHashMap();
        Optional.ofNullable(couponIdList).orElse(Collections.emptyList()).forEach(o -> {
            final int stock = Optional.ofNullable(couponMap.get(o)).map(Coupon::getNumberTotal).orElse(0) - Optional.ofNullable(couponDataMap.get(o)).map(CouponData::getNumberDistributed).orElse(0);
            resultMap.put(o, Math.max(stock, 0));
        });
        return resultMap;
    }

}
