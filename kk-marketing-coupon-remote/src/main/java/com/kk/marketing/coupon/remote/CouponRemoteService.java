package com.kk.marketing.coupon.remote;

import com.kk.marketing.coupon.dto.CouponDto;
import com.kk.marketing.coupon.vo.CouponVo;
import com.kk.arch.util.ResponseData;

import java.util.List;

/**
 * 券的管理操作
 */
public interface CouponRemoteService {

    ResponseData<Boolean> addCoupon(CouponVo couponVo);

    ResponseData<List<CouponDto>> listCoupon();
}
