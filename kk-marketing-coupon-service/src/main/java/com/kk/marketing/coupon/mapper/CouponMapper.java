package com.kk.marketing.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kk.marketing.coupon.entity.Coupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponMapper extends BaseMapper<Coupon> {

	List<Coupon> listCoupon(@Param("params") Coupon params);

	List<Coupon> queryPage(IPage<Coupon> page, @Param("params") Coupon params);
}
