package com.kk.marketing.coupon.service;

import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.req.CouponQueryReqDto;

import java.util.List;
import java.util.Map;

/**
 * @author Zal
 */
public interface CouponService extends BaseService<Coupon> {


    Map<Long, Coupon> queryMap(List<Long> idList);

    List<Coupon> queryList(CouponQueryReqDto reqDto);

    PageRespVo<Coupon> queryPage(PageReqVo<CouponQueryReqDto> pageReqVo);

}
