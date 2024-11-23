package com.kk.marketing.coupon.remote.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.kk.arch.common.util.*;
import com.kk.arch.common.vo.ResponseData;
import com.kk.marketing.coupon.aop.DistributedLock;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.entity.CouponUser;
import com.kk.marketing.coupon.enums.ActiveStatusEnum;
import com.kk.marketing.coupon.enums.CouponUserStatusEnum;
import com.kk.marketing.coupon.enums.UsableTimeTypeEnum;
import com.kk.marketing.coupon.remote.CouponDataRemote;
import com.kk.marketing.coupon.remote.CouponDistributionRemote;
import com.kk.marketing.coupon.req.CouponDistributeDetailReqDto;
import com.kk.marketing.coupon.req.CouponDistributionReqDto;
import com.kk.marketing.coupon.resp.DistributeCouponUserRespDto;
import com.kk.marketing.coupon.service.CouponDataService;
import com.kk.marketing.coupon.service.CouponService;
import com.kk.marketing.coupon.service.CouponUserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kk.marketing.coupon.req.CouponDistributionReqDto.MODE_TOLERANT;

/**
 * @author Zal
 */
@DubboService
public class CouponDistributeRemoteImpl implements CouponDistributionRemote {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponDataService couponDataService;

    @Autowired
    private CouponDataRemote couponDataRemote;

    @Autowired
    private CouponUserService couponUserService;

    protected void checkCoupon(CouponDistributionReqDto reqDto) {
        final List<Long> couponIdList = reqDto.getCouponList().stream().map(CouponDistributeDetailReqDto::getCouponId).distinct().toList();
        final Map<Long, Coupon> couponMap = couponService.queryMap(couponIdList);
        AssertUtils.isTrue(reqDto.getCouponList().size() == couponMap.size(), "有券未找到，发券失败");

        final List<Long> statusNotOpenIdList = couponMap.keySet().stream().filter(o -> ActiveStatusEnum.CLOSE.getCode() == couponMap.get(o).getActiveStatus()).toList();
        AssertUtils.isTrue(CollectionUtils.isEmpty(statusNotOpenIdList), "券" + JSON.toJSONString(statusNotOpenIdList) + "未启用，发券失败");
    }

    protected void checkStock(CouponDistributionReqDto reqDto) {
        if (reqDto.getMode() == MODE_TOLERANT) {
            return;
        }

        final List<Long> couponIdList = reqDto.getCouponList().stream().map(CouponDistributeDetailReqDto::getCouponId).distinct().toList();
        final Map<Long, Integer> couponStockMap = couponDataRemote.getCouponStockMap(reqDto.getTenantId(), couponIdList);
        for (CouponDistributeDetailReqDto distributeCouponReqDto : reqDto.getCouponList()) {
            final int shouldDistributeCouponNum = distributeCouponReqDto.getNum() * reqDto.getUserIdList().size();
            AssertUtils.isTrue(Optional.ofNullable(couponStockMap.get(distributeCouponReqDto.getCouponId())).orElse(0) >= shouldDistributeCouponNum, "券[" + distributeCouponReqDto.getCouponId() + "]库存不足，发券失败");
        }
    }

    protected void deductStock(CouponDistributionReqDto reqDto) {
        for (CouponDistributeDetailReqDto distributeCouponReqDto : reqDto.getCouponList()) {
            final boolean deductResult = couponDataService.increaseNumberDistributed(distributeCouponReqDto.getCouponId(), distributeCouponReqDto.getNum());
            AssertUtils.isTrue(deductResult, "券[" + distributeCouponReqDto.getCouponId() + "]库存扣减出错，发券失败");
        }
    }

    protected Date calculateUsableStartTime(Coupon coupon) {
        if (UsableTimeTypeEnum.FIXED.getCode() == coupon.getUsableTimeType()) {
            return coupon.getUsableFixedStart();
        } else {
            return DateUtils.addDays(DateUtils.getNow(), coupon.getUsableFlexFrom());
        }
    }

    protected Date calculateUsableEndTime(Coupon coupon) {
        if (UsableTimeTypeEnum.FIXED.getCode() == coupon.getUsableTimeType()) {
            return coupon.getUsableFixedEnd();
        } else {
            return DateUtils.addDays(DateUtils.getNow(), coupon.getUsableFlexFrom() + coupon.getUsableFlexDay());
        }
    }

    /**
     * 按用户userId来循环发放券
     */
    protected List<CouponUser> saveCouponUser(CouponDistributionReqDto reqDto) {
        final List<Long> couponIdList = reqDto.getCouponList().stream().map(CouponDistributeDetailReqDto::getCouponId).distinct().toList();
        final Map<Long, Coupon> couponMap = couponService.queryMap(couponIdList);

        List<CouponUser> couponUserList = Lists.newArrayList();
        reqDto.getUserIdList().forEach(userId -> {
            for (CouponDistributeDetailReqDto distributeCouponReqDto : reqDto.getCouponList()) {
                for (int i = 0; i < distributeCouponReqDto.getNum(); i++) {
                    final CouponUser couponUser = CouponUser.builder()
                            .tenantId(reqDto.getTenantId())
                            .couponCode(SnowflakeIdUtils.generateId().toString())
                            .couponId(distributeCouponReqDto.getCouponId())
                            .sourceActivityType(reqDto.getSourceActivityType())
                            .sourceActivityId(reqDto.getSourceActivityId())
                            .sourceActivityName(reqDto.getSourceActivityName())
                            .sourceStoreId(reqDto.getSourceStoreId())
                            .sourceStoreName(reqDto.getSourceStoreName())
                            .sourceOrderNbr(reqDto.getSourceOrderNbr())
                            .usableStartTime(this.calculateUsableStartTime(couponMap.get(distributeCouponReqDto.getCouponId())))
                            .usableEndTime(this.calculateUsableEndTime(couponMap.get(distributeCouponReqDto.getCouponId())))
                            .userId(userId)
                            .status(CouponUserStatusEnum.UNUSED.getCode())
                            .build();
                    couponUserList.add(couponUser);
                }
            }
        });

        final boolean saveResult = couponUserService.saveBatch(couponUserList);
        AssertUtils.isTrue(saveResult, "用户券保存出错，发券失败");

        return couponUserList;
    }

    /**
     * 同步发券，扣减券库存，再直接落库
     */
    @Override
    @Transactional
    @DistributedLock(key = "'syncDistributeCoupon_'+#reqDto.getTenantId()")
    public ResponseData<List<DistributeCouponUserRespDto>> syncDistributeCoupon(CouponDistributionReqDto reqDto) {
        // 1. 先检查参数
        final long couponIdCount = reqDto.getCouponList().stream().map(CouponDistributeDetailReqDto::getCouponId).distinct().count();
        AssertUtils.isTrue(reqDto.getCouponList().size() == couponIdCount, "券列表有重复的券id，请检查是否重复发放");

        // 2. 再检查券的状态和时间范围
        this.checkCoupon(reqDto);

        // 2. 再检查库查是不是足够
        this.checkStock(reqDto);

        // 3. 扣减库存
        this.deductStock(reqDto);

        // 4. 发券到会员用户
        List<CouponUser> couponUserList = this.saveCouponUser(reqDto);

        return ResponseUtils.success(JsonUtils.toList(couponUserList, DistributeCouponUserRespDto.class));
    }

    /**
     * 异步发券，扣减券库存，再发mq落库
     */
    @Override
    @Transactional
    @DistributedLock(key = "'syncDistributeCoupon_'+#reqDto.getTenantId()")
    public ResponseData<Boolean> asyncDistributeCoupon(CouponDistributionReqDto reqDto) {
        // 1. 先检查参数
        final long couponIdCount = reqDto.getCouponList().stream().map(CouponDistributeDetailReqDto::getCouponId).distinct().count();
        AssertUtils.isTrue(reqDto.getCouponList().size() == couponIdCount, "券列表有重复的券id，请检查是否重复发放");

        // 2. 再检查库查是不是足够
        this.checkStock(reqDto);

        // 3. 扣减库存
        this.deductStock(reqDto);

        // 4. 发送mq
        // mqService.send(reqDto);

        return ResponseUtils.success(true);
    }

    @Override
    public void sendCouponUserForMq(CouponDistributionReqDto reqDto) {
        this.saveCouponUser(reqDto);
    }

}
