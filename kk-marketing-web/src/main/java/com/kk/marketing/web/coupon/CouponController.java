package com.kk.marketing.web.coupon;

import com.kk.arch.util.PageReqVo;
import com.kk.arch.util.PageRespVo;
import com.kk.arch.util.ResponseData;
import com.kk.marketing.coupon.dto.CouponDto;
import com.kk.marketing.coupon.remote.CouponRemoteService;
import com.kk.marketing.coupon.vo.CouponVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CouponController {

    @DubboReference(lazy = true)
    private CouponRemoteService couponRemoteService;

    @PostMapping("/addCoupon")
    public ResponseData<Boolean> addCoupon() {
        return couponRemoteService.addCoupon(CouponVo.builder().build());
    }

    @PostMapping("/listCoupon")
    public ResponseData<List<CouponDto>> listCoupon() {
        return couponRemoteService.listCoupon();
    }

    @PostMapping("/queryPage")
    public ResponseData<PageRespVo<CouponDto>> queryPage(@RequestBody @Validated PageReqVo<CouponDto> pageReqVo) {
        return couponRemoteService.queryPage(pageReqVo);
    }

}
