package com.kk.marketing.coupon.remote.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kk.arch.dubbo.common.util.*;
import com.kk.arch.dubbo.remote.exception.BusinessException;
import com.kk.arch.dubbo.remote.vo.ResponseData;
import com.kk.marketing.coupon.adapter.ProductQueryAdapter;
import com.kk.marketing.coupon.algorithm.FullMinusCouponDpAlgorithm;
import com.kk.marketing.coupon.entity.ActivityGoods;
import com.kk.marketing.coupon.entity.ActivityStore;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.entity.CouponUser;
import com.kk.marketing.coupon.enums.*;
import com.kk.marketing.coupon.remote.CouponConsumeRemote;
import com.kk.marketing.coupon.req.*;
import com.kk.marketing.coupon.resp.CalculateDiscountRespDto;
import com.kk.marketing.coupon.resp.ConsumeQueryCouponUserRespDto;
import com.kk.marketing.coupon.resp.ConsumeQueryRespDto;
import com.kk.marketing.coupon.service.*;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.dubbo.common.utils.StringUtils.EMPTY_STRING;

/**
 * @author Zal
 */
@DubboService
public class CouponConsumeRemoteImpl implements CouponConsumeRemote {

    @Autowired
    private CouponService couponService;

    @Autowired
    private ActivityStoreService activityStoreService;

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @Autowired
    private CouponUserService couponUserService;

    @Autowired
    private ProductQueryAdapter productQueryAdapter;

    @Autowired
    private ConsumeCalculateDiscountService consumeCalculateDiscountService;

    @Override
    public ResponseData<ConsumeQueryRespDto> listCouponUser(ConsumeQueryReqDto reqDto) {
        List<ConsumeQueryCouponUserRespDto> couponUserRespDtoList = this.baseValidate(reqDto);

        List<ConsumeQueryCouponUserRespDto> usableList = couponUserRespDtoList.stream()
                                                                              .filter(ConsumeQueryCouponUserRespDto::getIsUsable)
                                                                              .sorted(ConsumeQueryCouponUserRespDto.customCompare())
                                                                              .toList();
        List<ConsumeQueryCouponUserRespDto> unusableList = couponUserRespDtoList.stream().filter(o -> !o.getIsUsable()).toList();
        List<ConsumeQueryCouponUserRespDto> recommendList = this.getRecommendList(usableList);

        final ConsumeQueryRespDto respDto = ConsumeQueryRespDto.builder().recommendList(recommendList).usableList(usableList).unusableList(unusableList).build();
        return ResponseUtils.success(respDto);
    }

    @Override
    public ResponseData<ConsumeQueryCouponUserRespDto> validClick(ConsumeValidClickReqDto reqDto) {
        return null;
    }

    @Override
    public ResponseData<ConsumeQueryCouponUserRespDto> validScan(ConsumeValidClickReqDto reqDto) {
        return null;
    }

    /**
     * 先查出所以可用的用户券，即有效时间内且状态是未使用的
     * 从券的维度进行判断是否适用当前平台、当前门店、当前时段周期、当前时段区间等等
     */
    private List<ConsumeQueryCouponUserRespDto> baseValidate(ConsumeQueryReqDto reqDto) {
        final List<CouponUser> couponUserList = couponUserService.listCanConsumeCouponUser(reqDto.getUserId());
        final List<Long> couponIdList = couponUserList.stream().map(CouponUser::getCouponId).distinct().toList();
        final Map<Long, Coupon> couponMap = couponService.queryMap(couponIdList);
        final Supplier<Map<Long, List<ActivityStore>>> activityStoreGroupMapSupplier = () -> activityStoreService.queryGroupMap(ActivityTypeEnum.COUPON.getCode(), couponIdList);
        final Supplier<Map<Long, List<ActivityGoods>>> activityGoodsGroupMapSupplier = () -> activityGoodsService.queryGroupMap(ActivityTypeEnum.COUPON.getCode(), couponIdList);

        final Map<Long, String> couponNotMetMap = Maps.newHashMap();
        final Map<Long, BigDecimal> couponDiscountMap = Maps.newHashMap();
        couponIdList.forEach(o -> {
            if (!isUsePlatformMet(couponMap.get(o), reqDto.getUsePlatform())) {
                couponNotMetMap.put(o, "平台列表不适用");
            } else if (!isStockMet(couponMap.get(o), activityStoreGroupMapSupplier.get().get(o), reqDto.getStoreId())) {
                couponNotMetMap.put(o, "门店不适用");
            } else if (!isPeriodWeekMet(couponMap.get(o))) {
                couponNotMetMap.put(o, "部分时段的星期不适用");
            } else if (!isPeriodTimeSegmentsMet(couponMap.get(o))) {
                couponNotMetMap.put(o, "部分时段的区段不适用");
            } else {
                final List<ConsumedGoodsReqDto> metShareableActivitiesGoodsList = filterMetGoodsByShareableActivities(couponMap.get(o), reqDto.getGoodsList());
                if (CollectionUtils.isEmpty(metShareableActivitiesGoodsList)) {
                    couponNotMetMap.put(o, "此券不能与商品参与的活动同时进行共享");
                }
                final List<ConsumedGoodsReqDto> metGoodsList = filterMetGoodsByActivityGoods(couponMap.get(o).getUseScope(), activityGoodsGroupMapSupplier.get().get(o), metShareableActivitiesGoodsList);
                if (CollectionUtils.isEmpty(metGoodsList)) {
                    couponNotMetMap.put(o, "此券没有适用的商品");
                } else if (metGoodsTotal(metGoodsList).compareTo(BigDecimal.ZERO) > 0) {
                    couponNotMetMap.put(o, "适用的商品的小计金额未达到门槛");
                } else {
                    // this coupon is usable for current order, so calculate the discount of this coupon
                    couponDiscountMap.put(o, this.getDiscount(couponMap.get(o), reqDto.getGoodsList()));
                }
            }
        });

        return couponUserList.stream().map(o -> {
            return ConsumeQueryCouponUserRespDto.builder()
                                                .couponCode(o.getCouponCode())
                                                .couponId(o.getCouponId())
                                                .isUsable(couponNotMetMap.get(o.getCouponId()) == null && couponDiscountMap.get(o.getCouponId()).compareTo(BigDecimal.ZERO) > 0)
                                                .unusableMsg(Optional.ofNullable(couponNotMetMap.get(o.getCouponId())).orElse(EMPTY_STRING))
                                                .discount(couponDiscountMap.get(o.getCouponId()).compareTo(BigDecimal.ZERO) > 0 ? couponDiscountMap.get(o.getCouponId()) : BigDecimal.ZERO)
                                                .useThreshold(couponMap.get(o.getCouponId()).getUseThreshold())
                                                .build();
        }).collect(Collectors.toList());
    }

    private BigDecimal metGoodsTotal(List<ConsumedGoodsReqDto> metGoodsList) {
        return metGoodsList.stream().map(o -> o.getQuantity().multiply(o.getPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<ConsumedGoodsReqDto> filterMetGoodsByShareableActivities(Coupon coupon, List<ConsumedGoodsReqDto> reqGoodsList) {
        if (StringUtils.isBlank(coupon.getShareActivityTypes())) {
            return reqGoodsList;
        } else {
            final List<Integer> activityTypeList = Arrays.stream(coupon.getShareActivityTypes().split(",")).map(Integer::parseInt).toList();
            return reqGoodsList.stream().filter(o -> activityTypeList.stream().anyMatch(p -> o.getActivityType() == null || o.getActivityType().equals(p))).collect(Collectors.toList());
        }
    }

    /**
     * 筛选出活动适用的商品列表
     */
    private List<ConsumedGoodsReqDto> filterMetGoodsByActivityGoods(Integer useScope, List<ActivityGoods> activityGoodsList, List<ConsumedGoodsReqDto> reqGoodsList) {
        final List<String> activityGoodsValueList = activityGoodsList.stream().map(ActivityGoods::getGoodsValue).toList();
        if (UseScopeEnum.SINGLE.getCode() == useScope) {
            return reqGoodsList.stream().filter(o -> activityGoodsValueList.contains(o.getUpc())).collect(Collectors.toList());
        } else if (UseScopeEnum.CATEGORY.getCode() == useScope) {
            final List<String> reqUpcList = reqGoodsList.stream().map(ConsumedGoodsReqDto::getUpc).toList();
            final Map<String, List<String>> categoryGroupMap = productQueryAdapter.queryCategoryGroupMap(reqUpcList);
            return reqGoodsList.stream().filter(o -> activityGoodsValueList.stream().anyMatch(p -> categoryGroupMap.get(o.getUpc()).contains(p))).collect(Collectors.toList());
        } else if (UseScopeEnum.BRAND.getCode() == useScope) {
            final List<String> reqUpcList = reqGoodsList.stream().map(ConsumedGoodsReqDto::getUpc).toList();
            final Map<String, List<String>> brandGroupMap = productQueryAdapter.queryBrandGroupMap(reqUpcList);
            return reqGoodsList.stream().filter(o -> activityGoodsValueList.stream().anyMatch(p -> brandGroupMap.get(o.getUpc()).contains(p))).collect(Collectors.toList());
        } else if (UseScopeEnum.WHOLE.getCode() == useScope) {
            return reqGoodsList;
        } else {
            throw new BusinessException(-1, "非法的使用范围");
        }
    }

    /**
     * 计算可用券的优惠金额
     */
    private BigDecimal getDiscount(Coupon coupon, List<ConsumedGoodsReqDto> metGoodsList) {
        if (CouponTypeEnum.FULL.getCode() == coupon.getCouponType()) {
            return BigDecimal.valueOf(coupon.getCouponValue()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        } else if (CouponTypeEnum.DISCOUNT.getCode() == coupon.getCouponType()) {
            final BigDecimal metGoodsTotal = metGoodsList.stream().map(o -> o.getQuantity().multiply(o.getPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
            return metGoodsTotal.multiply(BigDecimal.valueOf(coupon.getCouponValue()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
        } else if (CouponTypeEnum.GIFT.getCode() == coupon.getCouponType()) {
            return metGoodsList.stream().findFirst().map(ConsumedGoodsReqDto::getSubtotal).orElse(BigDecimal.valueOf(-1));
        } else if (CouponTypeEnum.FIXED.getCode() == coupon.getCouponType()) {
            return metGoodsList.stream().findFirst().map(o -> o.getSubtotal().subtract(BigDecimal.valueOf(coupon.getCouponValue()))).orElse(BigDecimal.valueOf(-1));
        } else {
            throw new BusinessException(-1, "券类型不存在");
        }
    }

    private boolean isUsePlatformMet(Coupon coupon, Integer reqUsePlatform) {
        return Arrays.stream(coupon.getUsePlatforms().split(",")).anyMatch(o -> Integer.parseInt(o) == reqUsePlatform);
    }

    private boolean isStockMet(Coupon coupon, List<ActivityStore> activityStoreList, Long reqStoreId) {
        final Supplier<Boolean> isStockMet = () -> StoreTypeEnum.PART.getCode() == coupon.getStoreType() && activityStoreList.stream().anyMatch(p -> p.getStoreId().equals(reqStoreId));
        return StoreTypeEnum.ALL.getCode() == coupon.getStoreType() || isStockMet.get();
    }

    private boolean isPeriodWeekMet(Coupon coupon) {
        if (PeriodTypeEnum.PART.getCode() == coupon.getPeriodType()) {
            final Date now = DateUtils.getNow();
            final Supplier<Boolean> isWeekMet = () -> Arrays.stream(coupon.getPeriodWeeks().split(",")).anyMatch(o -> Integer.parseInt(o) == DateUtils.getWeek(now));
            return StringUtils.isNotBlank(coupon.getPeriodWeeks()) && isWeekMet.get();
        } else {
            return true;
        }
    }

    private boolean isPeriodTimeSegmentsMet(Coupon coupon) {
        if (PeriodTypeEnum.PART.getCode() == coupon.getPeriodType()) {
            final Date now = DateUtils.getNow();
            final Supplier<Boolean> isTimeSegmentMet = () -> Arrays.stream(coupon.getPeriodTimeSegments().split(",")).anyMatch(o -> {
                final String[] segments = o.split("-");
                AssertUtils.isTrue(segments.length == 2, "时间段格式不正确");
                return DateUtils.isBetween(now, segments[0], segments[1]);
            });
            return StringUtils.isNotBlank(coupon.getPeriodWeeks()) && isTimeSegmentMet.get();
        } else {
            return true;
        }
    }

    private List<ConsumeQueryCouponUserRespDto> getRecommendList(List<ConsumeQueryCouponUserRespDto> couponUserRespDtoList) {
        final List<FullMinusCouponDpAlgorithm.FullMinusCoupon> fullMinusCouponList = couponUserRespDtoList.stream()
                                                                                                          .map(o -> new FullMinusCouponDpAlgorithm.FullMinusCoupon(o.getCouponCode(), o.getDiscount().multiply(BigDecimal.valueOf(100)).intValue(), o.getUseThreshold()))
                                                                                                          .collect(Collectors.toList());
        Map.Entry<Integer, List<FullMinusCouponDpAlgorithm.FullMinusCoupon>> maxDiscountCombination = FullMinusCouponDpAlgorithm.findMaxDiscountCombination(fullMinusCouponList, 550);
        System.out.println("最大优惠金额: " + maxDiscountCombination.getKey());
        System.out.println("对应的券组合:");
        maxDiscountCombination.getValue().forEach(System.out::println);
        return null;
    }

    /**
     * 在可用的券列表里，去一张一张按顺序点选，最后返回可用的与不可用的
     */
    @Override
    public ResponseData<List<ConsumeQueryCouponUserRespDto>> validClick(CalculateDiscountReqDto reqDto) {
        List<ConsumeQueryCouponUserRespDto> couponUserRespDtoList = this.baseValidate(reqDto);
        List<ConsumeQueryCouponUserRespDto> usableList = couponUserRespDtoList.stream()
                                                                              .filter(ConsumeQueryCouponUserRespDto::getIsUsable)
                                                                              .sorted(ConsumeQueryCouponUserRespDto.customCompare())
                                                                              .toList();

        final List<CalculateDiscountRespDto> respDtoList = this.calculateDiscountOneByOne(reqDto);
        // return reqDto.getCouponUserList().stream().sorted(ConsumeCouponUserReqDto::getNumber).collect(Collectors.toList());
        return ResponseData.<List<ConsumeQueryCouponUserRespDto>>builder().build();
    }

    /**
     * 在可用的券列表里，去一张一张按顺序扫描，最后返回可用的与不可用的
     */
    @Override
    public ResponseData<ConsumeQueryCouponUserRespDto> validScan(CalculateDiscountReqDto reqDto) {
        return null;
    }

    protected List<CalculateDiscountRespDto> calculateDiscountOneByOne(CalculateDiscountReqDto reqDto) {
        final List<String> couponCodeList = reqDto.getCouponUserList().stream().map(ConsumeCouponUserReqDto::getCouponCode).toList();
        final Map<String, CouponUser> couponUserMap = couponUserService.queryMap(couponCodeList);
        final List<Long> couponIdList = couponUserMap.values().stream().map(CouponUser::getCouponId).distinct().toList();
        final Map<Long, Coupon> couponMap = couponService.queryMap(couponIdList);

        final Map<Long, List<ActivityStore>> activityStoreGroupMap = activityStoreService.queryGroupMap(ActivityTypeEnum.COUPON.getCode(), couponIdList);
        final Map<Long, List<ActivityGoods>> activityGoodsGroupMap = activityGoodsService.queryGroupMap(ActivityTypeEnum.COUPON.getCode(), couponIdList);

        List<CalculateDiscountRespDto> respDtoList = Lists.newArrayList();
        reqDto.getCouponUserList().stream().sorted(Comparator.comparing(ConsumeCouponUserReqDto::getNumber)).forEach(o -> {
            consumeCalculateDiscountService.calculateDiscountByCouponCode(respDtoList, reqDto, couponUserMap, couponMap, activityStoreGroupMap, activityGoodsGroupMap, o.getCouponCode());
        });

        return respDtoList;
    }

    @Override
    public ResponseData<List<CalculateDiscountRespDto>> calculateDiscount(CalculateDiscountReqDto reqDto) {
        return ResponseUtils.success(this.calculateDiscountOneByOne(reqDto));
    }

    @Override
    public ResponseData<Boolean> lock(LockCouponUserReqDto reqDto) {
        return null;
    }

    @Override
    public ResponseData<Boolean> unlock(UnlockCouponUserReqDto reqDto) {
        return null;
    }

    @Override
    public ResponseData<Boolean> consume(ConsumeReqDto reqDto) {
        return null;
    }
}
