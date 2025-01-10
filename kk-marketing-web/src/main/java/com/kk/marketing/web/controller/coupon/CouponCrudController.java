package com.kk.marketing.web.controller.coupon;

import com.kk.arch.dubbo.common.util.CollectionUtils;
import com.kk.arch.dubbo.common.util.JsonUtils;
import com.kk.arch.dubbo.remote.vo.PageReqVo;
import com.kk.arch.dubbo.remote.vo.PageRespVo;
import com.kk.arch.dubbo.remote.vo.ResponseData;
import com.kk.marketing.coupon.remote.CouponCrudRemote;
import com.kk.marketing.coupon.req.ActiveStatusUpdateReqDto;
import com.kk.marketing.coupon.req.CouponAddReqDto;
import com.kk.marketing.coupon.req.CouponQueryReqDto;
import com.kk.marketing.coupon.vo.CouponVo;
import com.kk.marketing.web.controller.BaseController;
import com.kk.marketing.web.req.ActiveStatusUpdateReqVo;
import com.kk.marketing.web.req.CouponAddReqVo;
import com.kk.marketing.web.req.CouponQueryReqVo;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "券模板表", description = "券模板的增删改查接口")
public class CouponCrudController extends BaseController {

    @DubboReference(lazy = true)
    private CouponCrudRemote couponCrudRemote;

    @PostMapping("/addCoupon")
    public ResponseData<Boolean> addCoupon(@RequestBody @Validated CouponAddReqVo reqVo) {
        final CouponAddReqDto reqDto = JsonUtils.toObject(reqVo, CouponAddReqDto.class);
        reqDto.setTenantId(getTenantId());
        reqDto.setCreateBy(getUserId());
        return couponCrudRemote.addCoupon(reqDto);
    }

    @PostMapping("/batchDelete")
    public ResponseData<Boolean> batchDelete(@RequestBody @Validated List<Long> idList) {
        Assert.isTrue(CollectionUtils.isNotEmpty(idList), "要删除的id列表不能为空");
        return couponCrudRemote.batchDelete(getTenantId(), idList, getUserId());
    }

    @PostMapping("/activate")
    public ResponseData<Boolean> activate(@RequestBody @Validated ActiveStatusUpdateReqVo reqVo) {
        final ActiveStatusUpdateReqDto reqDto = JsonUtils.toObject(reqVo, ActiveStatusUpdateReqDto.class);
        reqDto.setTenantId(getTenantId());
        reqDto.setUpdateBy(getUserId());
        Assert.isTrue(Objects.nonNull(reqDto.getId()), "要启用的id不能为空");
        return couponCrudRemote.activate(reqDto);
    }

    @PostMapping("/deactivate")
    public ResponseData<Boolean> deactivate(@RequestBody @Validated ActiveStatusUpdateReqVo reqVo) {
        final ActiveStatusUpdateReqDto reqDto = JsonUtils.toObject(reqVo, ActiveStatusUpdateReqDto.class);
        reqDto.setTenantId(getTenantId());
        reqDto.setUpdateBy(getUserId());
        Assert.isTrue(Objects.nonNull(reqDto.getId()), "要禁用的id不能为空");
        return couponCrudRemote.deactivate(reqDto);
    }

    @PostMapping("/listCoupon")
    public ResponseData<List<CouponVo>> listCoupon(@RequestBody @Validated CouponQueryReqVo reqVo) {
        final CouponQueryReqDto reqDto = JsonUtils.toObject(reqVo, CouponQueryReqDto.class);
        reqDto.setTenantId(getTenantId());
        return couponCrudRemote.listCoupon(reqDto);
    }

    @PostMapping("/queryPage")
    public ResponseData<PageRespVo<CouponVo>> queryPage(@RequestBody @Validated PageReqVo<CouponQueryReqVo> pageReqVo) {
        final PageReqVo<CouponQueryReqDto> paramPageReqVo = JsonUtils.toPageReqVo(pageReqVo, CouponQueryReqDto.class);
        paramPageReqVo.getParam().setTenantId(getTenantId());
        return couponCrudRemote.queryPage(paramPageReqVo);
    }

}
