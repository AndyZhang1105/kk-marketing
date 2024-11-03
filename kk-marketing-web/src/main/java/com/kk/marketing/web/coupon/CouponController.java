package com.kk.marketing.web.coupon;

import com.kk.arch.util.ResponseData;
import com.kk.marketing.coupon.dto.CouponDto;
import com.kk.marketing.coupon.remote.CouponRemoteService;
import com.kk.marketing.coupon.vo.CouponVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CouponController {

    @DubboReference(lazy = true)
    private CouponRemoteService couponRemoteService;

    @GetMapping("/addCoupon")
    public ResponseData<Boolean> addCoupon() {
        return couponRemoteService.addCoupon(CouponVo.builder().build());
    }

    @GetMapping("/listCoupon")
    public ResponseData<List<CouponDto>> listCoupon() {
        return couponRemoteService.listCoupon();
    }

}
