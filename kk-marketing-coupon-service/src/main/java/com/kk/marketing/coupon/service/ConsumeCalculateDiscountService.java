package com.kk.marketing.coupon.service;

import com.kk.marketing.coupon.entity.ActivityGoods;
import com.kk.marketing.coupon.entity.ActivityStore;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.entity.CouponUser;
import com.kk.marketing.coupon.req.CalculateDiscountReqDto;
import com.kk.marketing.coupon.resp.CalculateDiscountRespDto;

import java.util.List;
import java.util.Map;

/**
 * @author Zal
 */
public interface ConsumeCalculateDiscountService {


    /**
     * 通过券码和请求参数计算返回值
     * @param respDtoList 返回列表
     * @param reqDto 请求参数
     * @param couponCode 券码
     */
    void calculateDiscountByCouponCode(List<CalculateDiscountRespDto> respDtoList, CalculateDiscountReqDto reqDto, Map<String, CouponUser> couponUserMap, Map<Long, Coupon> couponMap,
                                       Map<Long, List<ActivityStore>> activityStoreGroupMap, Map<Long, List<ActivityGoods>> activityGoodsGroupMap,
                                       String couponCode);

}
