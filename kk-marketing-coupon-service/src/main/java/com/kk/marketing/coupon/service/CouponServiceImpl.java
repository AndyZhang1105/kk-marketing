package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.arch.util.BeanUtils;
import com.kk.arch.util.DateUtils;
import com.kk.arch.util.PageReqVo;
import com.kk.arch.util.PageRespVo;
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
		return couponMapper.insert(coupon) > 0;
	}

	@Override
	public List<Coupon> listCoupon(Coupon params) {
		return couponMapper.listCoupon(params);
	}

	@Override
	public PageRespVo<Coupon> queryPage(PageReqVo<Coupon> pageReqVo) {
		Page<Coupon> page = new Page<>(pageReqVo.getPageNum(), pageReqVo.getPageSize());
		final Coupon coupon = BeanUtils.toObject(pageReqVo.getParam(), Coupon.class);
		List<Coupon> resultList = couponMapper.queryPage(page, coupon);
		return new PageRespVo<>(page.getTotal(), page.getCurrent(), page.getSize(), resultList);
	}

}
