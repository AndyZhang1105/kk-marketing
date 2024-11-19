package com.kk.marketing.coupon.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.kk.marketing.coupon.entity.CouponData;
import com.kk.marketing.coupon.mapper.CouponDataMapper;
import com.kk.marketing.coupon.service.CouponDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Zal
 */
@Service
public class CouponDataServiceImpl extends BaseServiceImpl<CouponDataMapper, CouponData> implements CouponDataService {

    @Autowired
    private CouponDataMapper couponDataMapper;

    @Override
    public Map<Long, CouponData> getCouponDataMap(Long tenantId, List<Long> couponIdList) {
        List<CouponData> resultList = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(CouponData::getTenantId, tenantId)
                .in(CouponData::getCouponId, couponIdList)
                .list();
        return resultList.stream().collect(Collectors.toMap(CouponData::getCouponId, Function.identity(), (f, s) -> f));
    }

    @Override
    public int getNumberDistributed(Long tenantId, Long couponId) {
        final Optional<CouponData> couponDataOptional = new LambdaQueryChainWrapper<>(couponDataMapper)
                .eq(CouponData::getTenantId, tenantId)
                .in(CouponData::getCouponId, couponId)
                .oneOpt();
        return couponDataOptional.map(CouponData::getNumberDistributed).orElse(0);
    }

    @Override
    public boolean increaseNumberDistributed(Long tenantId, Long couponId, Integer num) {
        return new LambdaUpdateChainWrapper<>(couponDataMapper)
                .eq(CouponData::getTenantId, tenantId)
                .eq(CouponData::getCouponId, couponId)
                .setIncrBy(CouponData::getNumberDistributed, num)
                .update() || this.save(CouponData.builder().tenantId(tenantId).couponId(couponId).numberDistributed(num).build());
    }

    @Override
    public boolean increaseNumberConsumed(Long tenantId, Long couponId, Integer num) {
        return new LambdaUpdateChainWrapper<>(couponDataMapper)
                .eq(CouponData::getTenantId, tenantId)
                .eq(CouponData::getCouponId, couponId)
                .setIncrBy(CouponData::getNumberConsumed, num)
                .update() || this.save(CouponData.builder().tenantId(tenantId).couponId(couponId).numberConsumed(num).build());
    }

}
