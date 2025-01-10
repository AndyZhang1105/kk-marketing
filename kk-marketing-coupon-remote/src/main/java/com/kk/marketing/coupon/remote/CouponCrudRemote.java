package com.kk.marketing.coupon.remote;

import com.kk.arch.dubbo.remote.vo.PageReqVo;
import com.kk.arch.dubbo.remote.vo.PageRespVo;
import com.kk.arch.dubbo.remote.vo.ResponseData;
import com.kk.marketing.coupon.req.ActiveStatusUpdateReqDto;
import com.kk.marketing.coupon.req.CouponAddReqDto;
import com.kk.marketing.coupon.req.CouponQueryReqDto;
import com.kk.marketing.coupon.vo.CouponVo;

import java.util.List;

/**
 * 券的管理操作
 *
 * @author Zal
 */
public interface CouponCrudRemote {

    ResponseData<Boolean> addCoupon(CouponAddReqDto reqDto);

    ResponseData<Boolean> activate(ActiveStatusUpdateReqDto reqDto);

    ResponseData<Boolean> deactivate(ActiveStatusUpdateReqDto reqDto);

    ResponseData<Boolean> batchDelete(Long tenantId, List<Long> idList, Long operatorId);

    ResponseData<List<CouponVo>> listCoupon(CouponQueryReqDto reqDto);

    ResponseData<PageRespVo<CouponVo>> queryPage(PageReqVo<CouponQueryReqDto> pageReqVo);
}
