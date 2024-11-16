package com.kk.marketing.web.controller.coupon;

import com.kk.arch.util.BeanUtils;
import com.kk.arch.util.ResponseUtils;
import com.kk.arch.vo.ResponseData;
import com.kk.marketing.coupon.remote.CouponDistributionRemote;
import com.kk.marketing.coupon.req.DistributeCouponReqDto;
import com.kk.marketing.coupon.req.DistributeCouponReqVo;
import com.kk.marketing.coupon.resp.DistributeCouponUserRespDto;
import com.kk.marketing.web.controller.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Zal
 */
@RequestMapping("/marketing/web/coupon")
@RestController
public class DistributeCouponController extends BaseController {

    @DubboReference(lazy = true)
    private CouponDistributionRemote couponDistributionRemote;

    @PostMapping("/distributeCoupon")
    public ResponseData<Boolean> distributeCoupon(HttpServletRequest request, @RequestBody @Validated DistributeCouponReqVo reqVo) {
        reqVo.setTenantId(getTenantId(request));
        reqVo.setOperatorId(getUserId(request));
        ResponseData<List<DistributeCouponUserRespDto>> responseData = couponDistributionRemote.syncDistributeCoupon(BeanUtils.toObject(reqVo, DistributeCouponReqDto.class));
        return responseData.getCode() == ResponseData.SUCCESS ? ResponseUtils.success(true) : ResponseUtils.fail(responseData.getMsg());
    }

}
