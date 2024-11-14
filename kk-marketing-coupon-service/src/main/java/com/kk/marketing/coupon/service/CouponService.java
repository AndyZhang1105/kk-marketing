package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.arch.vo.PageReqVo;
import com.kk.arch.vo.PageRespVo;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.vo.CouponVo;

import java.util.List;

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

    PageRespVo<Coupon> queryPage(PageReqVo<Coupon> pageReqVo);
}
