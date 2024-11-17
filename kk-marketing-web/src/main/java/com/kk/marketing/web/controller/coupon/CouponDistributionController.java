package com.kk.marketing.web.controller.coupon;

import com.kk.arch.common.util.AssertUtils;
import com.kk.arch.common.util.JsonUtils;
import com.kk.arch.common.util.ResponseUtils;
import com.kk.arch.common.vo.ResponseData;
import com.kk.marketing.coupon.remote.CouponDistributionRemote;
import com.kk.marketing.coupon.req.CouponDistributeDetailReqDto;
import com.kk.marketing.coupon.req.CouponDistributionReqDto;
import com.kk.marketing.coupon.resp.DistributeCouponUserRespDto;
import com.kk.marketing.web.controller.BaseController;
import com.kk.marketing.web.req.DistributeCouponReqVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.kk.marketing.coupon.req.CouponDistributionReqDto.MODE_STRICT;

/**
 * @author Zal
 */
@RequestMapping("/marketing/web/coupon")
@RestController
public class CouponDistributionController extends BaseController {

    @DubboReference(lazy = true, timeout = 10000)
    private CouponDistributionRemote couponDistributionRemote;

    @PostMapping("/distributeCoupon")
    public ResponseData<Boolean> distributeCoupon(@RequestBody @Validated DistributeCouponReqVo reqVo) {
        AssertUtils.isNotNull(reqVo.getSourceStoreId(), "发券门店id不能为空");
        CouponDistributionReqDto reqDto = CouponDistributionReqDto.builder()
                .couponList(JsonUtils.toList(reqVo.getCouponList(), CouponDistributeDetailReqDto.class))
                .userIdList(reqVo.getUserIdList())
                .sourceStoreId(reqVo.getSourceStoreId())
                .mode(MODE_STRICT)
                .build();
        ResponseData<List<DistributeCouponUserRespDto>> responseData = couponDistributionRemote.syncDistributeCoupon(reqDto);
        return responseData.getCode() == ResponseData.SUCCESS ? ResponseUtils.success(true) : ResponseUtils.fail(responseData.getMsg());
    }

}
