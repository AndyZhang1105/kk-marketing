package com.kk.marketing.coupon.service;

import com.kk.marketing.coupon.entity.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CouponService extends IService<Coupon> {

	Boolean addCoupon();

	List<Coupon> listCoupon(Coupon params);
}
