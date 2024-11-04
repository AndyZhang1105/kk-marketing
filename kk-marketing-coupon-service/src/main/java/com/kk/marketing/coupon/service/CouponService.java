package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.arch.util.PageReqVo;
import com.kk.arch.util.PageRespVo;
import com.kk.marketing.coupon.entity.Coupon;

import java.util.List;

public interface CouponService extends IService<Coupon> {

	Boolean addCoupon();

	List<Coupon> listCoupon(Coupon params);

	PageRespVo<Coupon> queryPage(PageReqVo<Coupon> pageReqVo);
}
