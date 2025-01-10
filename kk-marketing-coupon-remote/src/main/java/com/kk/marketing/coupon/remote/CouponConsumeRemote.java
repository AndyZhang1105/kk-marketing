package com.kk.marketing.coupon.remote;

import com.kk.arch.dubbo.remote.vo.ResponseData;
import com.kk.marketing.coupon.req.*;
import com.kk.marketing.coupon.resp.CalculateDiscountRespDto;
import com.kk.marketing.coupon.resp.ConsumeQueryCouponUserRespDto;
import com.kk.marketing.coupon.resp.ConsumeQueryRespDto;

import java.util.List;

/**
 * @author Zal
 */
public interface CouponConsumeRemote {

    /**
     * 列出推荐用券、用户的可用的用户优惠列表、不可用券列表
     * 如果是会员用券，则uesrId必填
     * 如果是非会员用券，则uesrId填0，则couponCode必填
     */
    ResponseData<ConsumeQueryRespDto> listCouponUser(ConsumeQueryReqDto reqDto);

    /**
     * 当点选一些用户券后，刷新当前可用的与不可用的券列表
     */
    ResponseData<ConsumeQueryCouponUserRespDto> validClick(ConsumeValidClickReqDto reqDto);

    /**
     * 当扫码一张用户券后，刷新当前可用的与不可用的券列表
     */
    ResponseData<ConsumeQueryCouponUserRespDto> validScan(ConsumeValidClickReqDto reqDto);

    ResponseData<List<ConsumeQueryCouponUserRespDto>> validClick(CalculateDiscountReqDto reqDto);

    ResponseData<ConsumeQueryCouponUserRespDto> validScan(CalculateDiscountReqDto reqDto);

    /**
     * 计算分摊金额
     * 按商品进行计算，并给出每个商品的用的券对应的优惠金额明细列表
     */
    ResponseData<List<CalculateDiscountRespDto>> calculateDiscount(CalculateDiscountReqDto reqDto);

    /**
     * 预核销用户券，即锁定用户的优惠券
     */
    ResponseData<Boolean> lock(LockCouponUserReqDto reqDto);

    /**
     * 解锁用户券
     */
    ResponseData<Boolean> unlock(UnlockCouponUserReqDto reqDto);

    /**
     * 核销用户券
     */
    ResponseData<Boolean> consume(ConsumeReqDto reqDto);

}
