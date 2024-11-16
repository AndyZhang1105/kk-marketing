package com.kk.marketing.web.controller.coupon;

import com.kk.arch.util.CollectionUtils;
import com.kk.arch.vo.PageReqVo;
import com.kk.arch.vo.PageRespVo;
import com.kk.arch.vo.ResponseData;
import com.kk.marketing.coupon.remote.CouponCrudRemote;
import com.kk.marketing.coupon.vo.CouponVo;
import com.kk.marketing.web.controller.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author Zal
 */
@RequestMapping("/marketing/web/coupon")
@RestController
public class CouponController extends BaseController {

    @DubboReference(lazy = true)
    private CouponCrudRemote couponCrudRemote;

    @PostMapping("/addCoupon")
    public ResponseData<Boolean> addCoupon(HttpServletRequest request, @RequestBody @Validated CouponVo couponVo) {
        couponVo.setTenantId(getTenantId(request));
        couponVo.setCreateBy(getUserId(request));
        return couponCrudRemote.addCoupon(couponVo);
    }

    @PostMapping("/batchDelete")
    public ResponseData<Boolean> batchDelete(HttpServletRequest request, @RequestBody @Validated List<Long> idList) {
        Assert.isTrue(CollectionUtils.isNotEmpty(idList), "要删除的id列表不能为空");
        return couponCrudRemote.batchDelete(getTenantId(request), idList, getUserId(request));
    }

    @PostMapping("/activate")
    public ResponseData<Boolean> activate(HttpServletRequest request, @RequestBody @Validated CouponVo couponVo) {
        couponVo.setTenantId(getTenantId(request));
        couponVo.setUpdateBy(getUserId(request));
        Assert.isTrue(Objects.nonNull(couponVo.getId()), "要启用的id不能为空");
        return couponCrudRemote.activate(couponVo);
    }

    @PostMapping("/deactivate")
    public ResponseData<Boolean> deactivate(HttpServletRequest request, @RequestBody @Validated CouponVo couponVo) {
        couponVo.setTenantId(getTenantId(request));
        couponVo.setUpdateBy(getUserId(request));
        Assert.isTrue(Objects.nonNull(couponVo.getId()), "要禁用的id不能为空");
        return couponCrudRemote.deactivate(couponVo);
    }

    @PostMapping("/listCoupon")
    public ResponseData<List<CouponVo>> listCoupon(HttpServletRequest request) {
        return couponCrudRemote.listCoupon(getTenantId(request));
    }

    @PostMapping("/queryPage")
    public ResponseData<PageRespVo<CouponVo>> queryPage(HttpServletRequest request, @RequestBody @Validated PageReqVo<CouponVo> pageReqVo) {
        pageReqVo.getParam().setTenantId(getTenantId(request));
        return couponCrudRemote.queryPage(pageReqVo);
    }

}
