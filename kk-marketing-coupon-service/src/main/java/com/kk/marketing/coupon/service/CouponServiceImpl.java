package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.arch.util.DateUtils;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.mapper.CouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

	@Autowired
	private CouponMapper couponMapper;

	@Override
	public Boolean addCoupon() {
		Coupon coupon = new Coupon();
		coupon.setCouponName(DateUtils.getNow().toString());
		return this.save(coupon);
	}

	@Override
	public List<Coupon> listCoupon(Coupon params) {
		return couponMapper.listCoupon(params);
	}

}
