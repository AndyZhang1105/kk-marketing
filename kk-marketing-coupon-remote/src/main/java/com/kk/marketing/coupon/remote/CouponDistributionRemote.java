package com.kk.marketing.coupon.remote;

import com.kk.arch.vo.ResponseData;
import com.kk.marketing.coupon.req.DistributeCouponReqDto;
import com.kk.marketing.coupon.resp.DistributeCouponUserRespDto;

import java.util.List;

/**
 * @author Zal
 */
public interface CouponDistributionRemote {

    /**
     * 异步发券时，供mq的监听消息后调用发券，这个时候不需要关心券库存
     */
    void sendCouponUserForMq(DistributeCouponReqDto reqDto);

    /**
     * 同步发券，先扣库存，保存用户券入库
     */
    ResponseData<List<DistributeCouponUserRespDto>> syncDistributeCoupon(DistributeCouponReqDto reqDto);

    /**
     * 异步发券，先扣库存，再发mq
     */
    ResponseData<Boolean> asyncDistributeCoupon(DistributeCouponReqDto reqDto);

}
