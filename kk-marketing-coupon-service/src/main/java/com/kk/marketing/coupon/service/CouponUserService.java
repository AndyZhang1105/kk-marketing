package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.arch.dubbo.remote.vo.PageReqVo;
import com.kk.arch.dubbo.remote.vo.PageRespVo;
import com.kk.marketing.coupon.entity.CouponUser;
import com.kk.marketing.coupon.vo.CouponUserVo;

import java.util.List;
import java.util.Map;

/**
 * @author Zal
 */
public interface CouponUserService extends IService<CouponUser> {
    CouponUser queryOne(CouponUserVo couponUserVo);

    List<CouponUser> queryList(CouponUserVo couponUserVo);

    List<CouponUser> queryList(Long tenantId, List<Long> idList);

    Map<Long, CouponUser> queryMap(Long tenantId, List<Long> idList);

    PageRespVo<CouponUser> queryPage(PageReqVo<CouponUser> pageReqVo);

    List<CouponUser> listCanConsumeCouponUser(Long userId);

    Map<String, CouponUser> queryMap(List<String> couponCodeList);
}
