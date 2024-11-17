package com.kk.marketing.coupon.remote.impl;

import com.kk.arch.common.util.JsonUtils;
import com.kk.arch.common.util.ResponseUtils;
import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;
import com.kk.arch.common.vo.ResponseData;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.remote.CouponCrudRemote;
import com.kk.marketing.coupon.service.CouponService;
import com.kk.marketing.coupon.vo.CouponVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Zal
 */
@DubboService
public class CouponCrudRemoteImpl implements CouponCrudRemote {

    @Autowired
    private CouponService couponService;

    @Override
    public ResponseData<Boolean> addCoupon(CouponVo couponVo) {
        return ResponseUtils.success(couponService.addCoupon(couponVo));
    }

    @Override
    public ResponseData<Boolean> activate(CouponVo couponVo) {
        return ResponseUtils.success(couponService.activate(couponVo));
    }

    @Override
    public ResponseData<Boolean> deactivate(CouponVo couponVo) {
        return ResponseUtils.success(couponService.deactivate(couponVo));
    }

    @Override
    @Transactional
    public ResponseData<Boolean> batchDelete(Long tenantId, List<Long> idList, Long userId) {
        Optional.ofNullable(idList).orElse(Collections.emptyList()).forEach(o -> {
            final boolean result = couponService.delete(CouponVo.builder().id(o).tenantId(tenantId).updateBy(userId).build());
            Assert.isTrue(result, "批量删除失败");
        });

        return ResponseUtils.success(true);
    }

    @Override
    public ResponseData<List<CouponVo>> listCoupon(Long tenantId) {
        CouponVo couponVo = new CouponVo();
        couponVo.setTenantId(tenantId);
        final List<CouponVo> couponDtoList = JsonUtils.toList(couponService.queryList(couponVo), CouponVo.class);
        return ResponseUtils.success(couponDtoList);
    }

    @Override
    public ResponseData<PageRespVo<CouponVo>> queryPage(PageReqVo<CouponVo> pageReqVo) {
        final Coupon entityParam = JsonUtils.toObject(pageReqVo.getParam(), Coupon.class);
        PageReqVo<Coupon> newReqVo = PageReqVo.of(pageReqVo.getPageNum(), pageReqVo.getPageSize(), entityParam);
        final PageRespVo<Coupon> dataPageVo = couponService.queryPage(newReqVo);

        final List<CouponVo> couponVoList = JsonUtils.toList(dataPageVo.getList(), CouponVo.class);
        PageRespVo<CouponVo> resultPageVo = JsonUtils.toObject(dataPageVo, PageRespVo.class);
        resultPageVo.setList(couponVoList);

        return ResponseUtils.success(resultPageVo);
    }
}
