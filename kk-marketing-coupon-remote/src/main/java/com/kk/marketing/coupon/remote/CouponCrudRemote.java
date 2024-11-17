package com.kk.marketing.coupon.remote;

import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;
import com.kk.arch.common.vo.ResponseData;
import com.kk.marketing.coupon.vo.CouponVo;

import java.util.List;

/**
 * 券的管理操作
 *
 * @author Zal
 */
public interface CouponCrudRemote {

    ResponseData<Boolean> addCoupon(CouponVo couponVo);

    ResponseData<Boolean> activate(CouponVo couponVo);

    ResponseData<Boolean> deactivate(CouponVo couponVo);

    ResponseData<Boolean> batchDelete(Long tenantId, List<Long> idList, Long userId);

    ResponseData<List<CouponVo>> listCoupon(Long tenantId);

    ResponseData<PageRespVo<CouponVo>> queryPage(PageReqVo<CouponVo> pageReqVo);
}
