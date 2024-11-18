package com.kk.marketing.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.req.CouponQueryReqDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zal
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    List<Coupon> queryList(@Param("params") CouponQueryReqDto reqDto);

    List<Coupon> queryPage(IPage<Coupon> page, @Param("params") CouponQueryReqDto reqDto);

}
