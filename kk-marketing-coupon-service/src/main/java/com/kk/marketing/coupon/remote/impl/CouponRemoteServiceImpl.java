package com.kk.marketing.coupon.remote.impl;

import com.kk.arch.util.*;
import com.kk.arch.vo.*;
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
        return ResponseUtils.success(couponService.addCoupon());
    }

    @Override
    public ResponseData<List<CouponDto>> listCoupon() {
        Coupon params = new Coupon();
        final List<CouponDto> couponDtoList = BeanUtils.toList(couponService.listCoupon(params), CouponDto.class);
        return ResponseUtils.success(couponDtoList);
    }

    @Override
    public ResponseData<PageRespVo<CouponDto>> queryPage(PageReqVo<CouponDto> pageReqVo) {
        final Coupon entityParam = BeanUtils.toObject(pageReqVo.getParam(), Coupon.class);
        PageReqVo<Coupon> newReqVo = PageReqVo.of(pageReqVo.getPageNum(), pageReqVo.getPageSize(), entityParam);
        final PageRespVo<Coupon> dataPageVo = couponService.queryPage(newReqVo);

        final List<CouponDto> couponDtoList = BeanUtils.toList(dataPageVo.getList(), CouponDto.class);
        PageRespVo<CouponDto> resultPageVo = BeanUtils.toObject(dataPageVo, PageRespVo.class);
        resultPageVo.setList(couponDtoList);

        return ResponseUtils.success(resultPageVo);
    }
}
