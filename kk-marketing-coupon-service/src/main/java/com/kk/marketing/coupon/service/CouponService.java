package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.vo.CouponVo;

import java.util.List;
import java.util.Map;

/**
 * @author Zal
 */
public interface CouponService extends IService<Coupon> {

    Boolean addCoupon(CouponVo couponVo);

    Boolean activate(CouponVo couponVo);

    Boolean deactivate(CouponVo couponVo);

    boolean delete(CouponVo couponVo);

    Coupon queryOne(CouponVo couponVo);

    List<Coupon> queryList(CouponVo couponVo);

    List<Coupon> queryList(Long tenantId, List<Long> idList);

    Map<Long, Coupon> queryMap(Long tenantId, List<Long> idList);

    PageRespVo<Coupon> queryPage(PageReqVo<Coupon> pageReqVo);
}
