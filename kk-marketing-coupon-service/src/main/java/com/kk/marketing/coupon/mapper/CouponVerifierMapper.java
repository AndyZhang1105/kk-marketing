package com.kk.marketing.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kk.marketing.coupon.entity.CouponVerifier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 券核销员信息表 Mapper 接口
 * </p>
 *
 * @author Zal
 * @since 2024-11-19
 */
public interface CouponVerifierMapper extends BaseMapper<CouponVerifier> {

    List<CouponVerifier> queryList(@Param("name") String name);

}
