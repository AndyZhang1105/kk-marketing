package com.kk.marketing.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kk.marketing.coupon.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {

	List<Coupon> listCoupon(@Param("params") Coupon params);

}
