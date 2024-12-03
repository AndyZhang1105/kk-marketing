package com.kk.marketing.coupon.remote.impl;

import com.kk.arch.common.util.AssertUtils;
import com.kk.arch.common.util.JsonUtils;
import com.kk.arch.common.util.ResponseUtils;
import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;
import com.kk.arch.common.vo.ResponseData;
import com.kk.marketing.coupon.aop.MultiLevelCache;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.enums.ActiveStatusEnum;
import com.kk.marketing.coupon.enums.PeriodTypeEnum;
import com.kk.marketing.coupon.enums.UsableTimeTypeEnum;
import com.kk.marketing.coupon.remote.CouponCrudRemote;
import com.kk.marketing.coupon.req.ActiveStatusUpdateReqDto;
import com.kk.marketing.coupon.req.CouponAddReqDto;
import com.kk.marketing.coupon.req.CouponQueryReqDto;
import com.kk.marketing.coupon.service.CouponService;
import com.kk.marketing.coupon.vo.CouponVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Zal
 */
@DubboService
public class CouponCrudRemoteImpl implements CouponCrudRemote {

    @Autowired
    private CouponService couponService;

    @Override
    public ResponseData<Boolean> addCoupon(CouponAddReqDto reqDto) {
        // 1. check parameters is valid or not
        final UsableTimeTypeEnum usableTimeTypeEnum = UsableTimeTypeEnum.getByCode(reqDto.getUsableTimeType());
        AssertUtils.isTrue(usableTimeTypeEnum != null, "非法的券可用时间类型");
        if (usableTimeTypeEnum.getCode() == UsableTimeTypeEnum.FIXED.getCode()) {
            AssertUtils.isNotNull(reqDto.getUsableFixedStart(), "券可用开始时间不能为空");
            AssertUtils.isNotNull(reqDto.getUsableFixedEnd(), "券可用结束时间不能为空");
        } else {
            AssertUtils.isNotNull(reqDto.getUsableFlexFrom(), "券自领取后x天起算不能为空");
            AssertUtils.isNotNull(reqDto.getUsableFlexDay(), "券自领取后有效期x天不能为空");
        }

        if (PeriodTypeEnum.PART.getCode() == reqDto.getPeriodType()) {
            AssertUtils.isNotBlank(reqDto.getPeriodWeeks(), "可使用星期列表不能为空");
            AssertUtils.isNotBlank(reqDto.getPeriodTimeSegments(), "可使用的时间段列表不能为空");
        } else {
            reqDto.setPeriodWeeks("");
            reqDto.setPeriodTimeSegments("");
        }

        if (ActiveStatusEnum.OPEN.getCode() == reqDto.getStockWarnEnabled()) {
            AssertUtils.isTrue(reqDto.getStockSafeQuantity() != null, "券安全库存不能为空");
            AssertUtils.isTrue(reqDto.getStockSafeQuantity() > 0, "券安全库存必须大于0");
            AssertUtils.isNotBlank(reqDto.getStockMsgPhone(), "券安全库存通知手机不能为空");
        } else {
            reqDto.setStockSafeQuantity(0);
            reqDto.setStockMsgPhone("");
        }

        return ResponseUtils.success(couponService.addOne(reqDto));
    }

    @Override
    public ResponseData<Boolean> activate(ActiveStatusUpdateReqDto reqDto) {
        return ResponseUtils.success(couponService.activate(reqDto.getId()));
    }

    @Override
    public ResponseData<Boolean> deactivate(ActiveStatusUpdateReqDto reqDto) {
        return ResponseUtils.success(couponService.deactivate(reqDto.getId()));
    }

    @Override
    @Transactional
    public ResponseData<Boolean> batchDelete(Long tenantId, List<Long> idList, Long operatorId) {
        final boolean result = couponService.delete(idList, operatorId);
        Assert.isTrue(result, "批量删除目标失败");
        return ResponseUtils.success(true);
    }

    @Override
    @MultiLevelCache(ttl = 100)
    public ResponseData<List<CouponVo>> listCoupon(CouponQueryReqDto reqDto) {
        final List<CouponVo> couponDtoList = JsonUtils.toList(couponService.queryList(reqDto), CouponVo.class);
        return ResponseUtils.success(couponDtoList);
    }

    @Override
    public ResponseData<PageRespVo<CouponVo>> queryPage(PageReqVo<CouponQueryReqDto> pageReqVo) {
        final PageRespVo<Coupon> dataPageVo = couponService.queryPage(pageReqVo);
        return ResponseUtils.success(JsonUtils.toPageRespVo(dataPageVo, CouponVo.class));
    }
}
