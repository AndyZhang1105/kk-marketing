package com.kk.marketing.coupon.remote;

import com.kk.arch.vo.PageReqVo;
import com.kk.arch.vo.PageRespVo;
import com.kk.marketing.coupon.dto.CouponDto;
import com.kk.marketing.coupon.vo.CouponVo;
import com.kk.arch.vo.ResponseData;

import java.util.List;

/**
 * 券的管理操作
 */
public interface CouponRemoteService {

    ResponseData<Boolean> addCoupon(CouponVo couponVo);

    ResponseData<List<CouponDto>> listCoupon();

	ResponseData<PageRespVo<CouponDto>> queryPage(PageReqVo<CouponDto> pageReqVo);
}
