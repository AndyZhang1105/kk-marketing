package com.kk.marketing.coupon.remote.impl;

import com.kk.arch.util.BeanUtils;
import com.kk.arch.util.ResponseData;
import com.kk.arch.util.ResponseDataUtils;
import com.kk.marketing.coupon.dto.CouponDto;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.remote.CouponRemoteService;
import com.kk.marketing.coupon.service.CouponService;
import com.kk.marketing.coupon.vo.CouponVo;
import org.apache.dubbo.config.annotation.DubboService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class CouponRemoteServiceImpl implements CouponRemoteService {

    @Autowired
    private CouponService couponService;

    @Override
    public ResponseData<Boolean> addCoupon(CouponVo couponVo) {
        return ResponseDataUtils.success(couponService.addCoupon());
    }

    @Override
    public ResponseData<List<CouponDto>> listCoupon() {
        Coupon params = new Coupon();
        final List<CouponDto> couponDtoList = BeanUtils.toList(couponService.listCoupon(params), CouponDto.class);
        return ResponseDataUtils.success(couponDtoList);
    }
}
