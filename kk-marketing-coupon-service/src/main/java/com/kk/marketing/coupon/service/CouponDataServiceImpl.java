package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.marketing.coupon.entity.CouponData;
import com.kk.marketing.coupon.mapper.CouponDataMapper;
import org.springframework.stereotype.Service;

/**
 * @author Zal
 */
@Service
public class CouponDataServiceImpl extends ServiceImpl<CouponDataMapper, CouponData> implements CouponDataService {

    @Override
    public int getCouponStock(Long tenantId, Long couponId) {
        return 0;
    }

    @Override
    public boolean deductStock(Long tenantId, Long couponId, Integer num) {
        return true;
    }

}
