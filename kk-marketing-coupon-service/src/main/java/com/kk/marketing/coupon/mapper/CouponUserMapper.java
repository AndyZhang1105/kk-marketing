package com.kk.marketing.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kk.marketing.coupon.entity.CouponUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zal
 */
public interface CouponUserMapper extends BaseMapper<CouponUser> {

    List<CouponUser> queryList(@Param("params") CouponUser params);

    List<CouponUser> queryPage(IPage<CouponUser> page, @Param("params") CouponUser params);

}
