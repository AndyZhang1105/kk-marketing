package com.kk.marketing.coupon.service.impl;

import com.kk.arch.dubbo.common.util.AssertUtils;
import com.kk.arch.dubbo.common.util.CollectionUtils;
import com.kk.arch.dubbo.remote.exception.BusinessException;
import com.kk.arch.dubbo.remote.vo.ResponseData;
import com.kk.marketing.coupon.adapter.ProductQueryAdapter;
import com.kk.marketing.coupon.entity.ActivityGoods;
import com.kk.marketing.coupon.entity.ActivityStore;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.entity.CouponUser;
import com.kk.marketing.coupon.enums.CouponTypeEnum;
import com.kk.marketing.coupon.enums.UseScopeEnum;
import com.kk.marketing.coupon.req.CalculateDiscountReqDto;
import com.kk.marketing.coupon.req.ConsumedGoodsReqDto;
import com.kk.marketing.coupon.resp.CalculateDiscountRespDto;
import com.kk.marketing.coupon.service.ConsumeCalculateDiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kk.arch.dubbo.common.constant.CommonConstants.NO;
import static com.kk.arch.dubbo.common.constant.CommonConstants.YES;

/**
 * @author Zal
 */
@Service
@Slf4j
public class ConsumeCalculateDiscountServiceImpl implements ConsumeCalculateDiscountService {

    @Autowired
    private ProductQueryAdapter productQueryAdapter;

    /**
     * 优惠金额计算，这里计算有优先顺序的，单品券 > 商品券 > 全场券
     * 单品券：兑换券 > 定额券
     * 商品券：品类券 > 品牌券
     */
    @Override
    public void calculateDiscountByCouponCode(List<CalculateDiscountRespDto> respDtoList, CalculateDiscountReqDto reqDto, Map<String, CouponUser> couponUserMap, Map<Long, Coupon> couponMap,
                                              Map<Long, List<ActivityStore>> activityStoreGroupMap, Map<Long, List<ActivityGoods>> activityGoodsGroupMap, String couponCode) {
        CouponUser couponUser = couponUserMap.get(couponCode);
        if (couponUser != null) {
            Coupon coupon = couponMap.get(couponUser.getCouponId());
            AssertUtils.isNotNull(coupon, "没有找到对应的券模板");

            List<Long> activitStoreIdList = activityStoreGroupMap.get(coupon.getId()).stream().map(ActivityStore::getStoreId).distinct().toList();
            AssertUtils.isTrue(activitStoreIdList.contains(reqDto.getStoreId()), "券码[" + couponCode + "]不适用于此门店");

            if (CouponTypeEnum.GIFT.getCode() == coupon.getCouponType()) {
                this.calculateDiscountForGift(respDtoList, reqDto, couponUser, coupon, activityGoodsGroupMap);
            } else if (CouponTypeEnum.FIXED.getCode() == coupon.getCouponType()) {
                this.calculateDiscountForFixed(respDtoList, reqDto, couponUser, coupon, activityGoodsGroupMap);
            } else if (CouponTypeEnum.FULL.getCode() == coupon.getCouponType()) {
                this.calculateDiscountForFull(respDtoList, reqDto, couponUser, coupon, activityGoodsGroupMap);
            } else if (CouponTypeEnum.DISCOUNT.getCode() == coupon.getCouponType()) {
                this.calculateDiscountForDiscount(respDtoList, reqDto, couponUser, coupon, activityGoodsGroupMap);
            } else {
                throw new BusinessException(ResponseData.FAIL, "券类型非法" + couponCode);
            }
        } else {
            throw new BusinessException(ResponseData.FAIL, "未找到此券码[]" + couponCode);
        }
    }

    private void addCouponToRespList(List<CalculateDiscountRespDto> respDtoList, CouponUser couponUser, ConsumedGoodsReqDto goodsReqDto, BigDecimal discount) {
        respDtoList.add(CalculateDiscountRespDto.builder().number(goodsReqDto.getNumber()).upc(goodsReqDto.getUpc()).discount(discount).couponCode(couponUser.getCouponCode()).build());
    }

    private void calculateDiscountForGift(List<CalculateDiscountRespDto> respDtoList, CalculateDiscountReqDto reqDto, CouponUser couponUser, Coupon coupon, Map<Long, List<ActivityGoods>> activityGoodsGroupMap) {
        String activityGoodsUpc = activityGoodsGroupMap.get(coupon.getId()).stream().map(ActivityGoods::getGoodsValue).distinct().findFirst().orElse(null);
        AssertUtils.isNotNull(activityGoodsUpc, "券未配置兑换的商品");
        for (ConsumedGoodsReqDto goodsReqDto : reqDto.getGoodsList()) {
            if (goodsReqDto.getUpc().equals(activityGoodsUpc)) {
                if (goodsReqDto.getQuantity().compareTo(BigDecimal.ONE) >= 0) {
                    this.addCouponToRespList(respDtoList, couponUser, goodsReqDto, goodsReqDto.getPrice());
                    goodsReqDto.setQuantity(goodsReqDto.getQuantity().subtract(BigDecimal.ONE));
                    goodsReqDto.setSubtotal(goodsReqDto.getSubtotal().subtract(goodsReqDto.getPrice()));
                } else {
                    throw new BusinessException(ResponseData.FAIL, "兑换券码[" + couponUser.getCouponCode() + "]找到对应的商品数量小于1");
                }
            }
        }
        throw new BusinessException(ResponseData.FAIL, "兑换券码[" + couponUser.getCouponCode() + "]未能找到对应的商品");
    }

    private void calculateDiscountForFixed(List<CalculateDiscountRespDto> respDtoList, CalculateDiscountReqDto reqDto, CouponUser couponUser, Coupon coupon, Map<Long, List<ActivityGoods>> activityGoodsGroupMap) {
        String activityGoodsUpc = activityGoodsGroupMap.get(coupon.getId()).stream().map(ActivityGoods::getGoodsValue).distinct().findFirst().orElse(null);
        AssertUtils.isNotNull(activityGoodsUpc, "券未配置定额的商品");
        for (ConsumedGoodsReqDto goodsReqDto : reqDto.getGoodsList()) {
            if (goodsReqDto.getUpc().equals(activityGoodsUpc)) {
                AssertUtils.isTrue(goodsReqDto.getPrice().compareTo(new BigDecimal(coupon.getCouponValue())) > 0, "商品单价[" + goodsReqDto.getPrice().toString() + "]需要大于定额券面值[" + coupon.getCouponValue() + "]");
                if (goodsReqDto.getQuantity().compareTo(BigDecimal.ONE) >= 0) {
                    this.addCouponToRespList(respDtoList, couponUser, goodsReqDto, new BigDecimal(coupon.getCouponValue()).subtract(goodsReqDto.getPrice()));
                    goodsReqDto.setQuantity(goodsReqDto.getQuantity().subtract(BigDecimal.ONE));
                    goodsReqDto.setSubtotal(goodsReqDto.getSubtotal().subtract(goodsReqDto.getPrice()));
                } else {
                    throw new BusinessException(ResponseData.FAIL, "定额券码[" + couponUser.getCouponCode() + "]找到对应的商品数量小于1");
                }
            }
        }
        throw new BusinessException(ResponseData.FAIL, "定额券码[" + couponUser.getCouponCode() + "]未能找到对应的商品");
    }

    private List<ConsumedGoodsReqDto> getMetGoodsList(List<CalculateDiscountRespDto> respDtoList, CalculateDiscountReqDto reqDto, CouponUser couponUser, Coupon coupon, Map<Long, List<ActivityGoods>> activityGoodsGroupMap) {
        final List<ConsumedGoodsReqDto> reqGoodsList = reqDto.getGoodsList().stream().filter(o -> o.getSubtotal().compareTo(BigDecimal.ZERO) > 0).toList();
        if (coupon.getUseScope() == UseScopeEnum.SINGLE.getCode()) {
            List<String> availableActivityGoodsUpcList = activityGoodsGroupMap.get(coupon.getId()).stream().filter(o -> o.getIsAvailable() == YES).map(ActivityGoods::getGoodsValue).distinct().toList();
            List<String> nAvailableActivityGoodsUpcList = activityGoodsGroupMap.get(coupon.getId()).stream().filter(o -> o.getIsAvailable() == NO).map(ActivityGoods::getGoodsValue).distinct().toList();
            return reqGoodsList.stream().filter(o -> availableActivityGoodsUpcList.contains(o.getUpc()) && !nAvailableActivityGoodsUpcList.contains(o.getUpc())).toList();
        } else if (coupon.getUseScope() == UseScopeEnum.CATEGORY.getCode()) {
            List<String> availableActivityGoodsCategoryCodeList = activityGoodsGroupMap.get(coupon.getId()).stream().filter(o -> o.getIsAvailable() == YES).map(ActivityGoods::getGoodsValue).distinct().toList();
            List<String> nAvailableActivityGoodsCategoryCodeList = activityGoodsGroupMap.get(coupon.getId()).stream().filter(o -> o.getIsAvailable() == NO).map(ActivityGoods::getGoodsValue).distinct().toList();
            Map<String, List<String>> reqGoodsCategoryCodeGroupMap = productQueryAdapter.queryCategoryGroupMap(reqGoodsList.stream().map(ConsumedGoodsReqDto::getUpc).collect(Collectors.toList()));
            return reqGoodsList.stream()
                                   .filter(o -> CollectionUtils.anyMatch(reqGoodsCategoryCodeGroupMap.get(o.getUpc()), availableActivityGoodsCategoryCodeList))
                                   .filter(o -> CollectionUtils.noneMatch(reqGoodsCategoryCodeGroupMap.get(o.getUpc()), nAvailableActivityGoodsCategoryCodeList))
                                   .toList();
        } else if (coupon.getUseScope() == UseScopeEnum.BRAND.getCode()) {
            List<String> availableActivityGoodsBrandCodeList = activityGoodsGroupMap.get(coupon.getId()).stream().filter(o -> o.getIsAvailable() == YES).map(ActivityGoods::getGoodsValue).distinct().toList();
            List<String> nAvailableActivityGoodsBrandCodeList = activityGoodsGroupMap.get(coupon.getId()).stream().filter(o -> o.getIsAvailable() == NO).map(ActivityGoods::getGoodsValue).distinct().toList();
            Map<String, List<String>> reqGoodsCategoryCodeGroupMap = productQueryAdapter.queryBrandGroupMap(reqGoodsList.stream().map(ConsumedGoodsReqDto::getUpc).collect(Collectors.toList()));
            return reqGoodsList.stream()
                                   .filter(o -> CollectionUtils.anyMatch(reqGoodsCategoryCodeGroupMap.get(o.getUpc()), availableActivityGoodsBrandCodeList))
                                   .filter(o -> CollectionUtils.noneMatch(reqGoodsCategoryCodeGroupMap.get(o.getUpc()), nAvailableActivityGoodsBrandCodeList))
                                   .toList();
        } else if (coupon.getUseScope() == UseScopeEnum.WHOLE.getCode()) {
            return reqDto.getGoodsList();
        } else {
            throw new BusinessException(ResponseData.FAIL, "满减券码[" + couponUser.getCouponCode() + "]的使用范围非法");
        }
    }

    /**
     * 满减券的优惠金额计算, 减去门槛后可再分摊别的券
     */
    private void calculateDiscountForFull(List<CalculateDiscountRespDto> respDtoList, CalculateDiscountReqDto reqDto, CouponUser couponUser, Coupon coupon, Map<Long, List<ActivityGoods>> activityGoodsGroupMap) {
        final List<ConsumedGoodsReqDto> metGoods = this.getMetGoodsList(respDtoList, reqDto, couponUser, coupon, activityGoodsGroupMap);

        // 算出本次券码适用的商品对应的小计金额
        final BigDecimal metGoodsSubtotal = metGoods.stream().map(ConsumedGoodsReqDto::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (metGoodsSubtotal.compareTo(BigDecimal.valueOf(coupon.getUseThreshold())) >= 0) {
            metGoods.forEach(o -> {
                final BigDecimal goodsPercent = o.getSubtotal().divide(metGoodsSubtotal, 20, RoundingMode.HALF_UP);
                this.addCouponToRespList(respDtoList, couponUser, o, new BigDecimal(coupon.getCouponValue()).multiply(goodsPercent));
                o.setSubtotal(o.getSubtotal().subtract(BigDecimal.valueOf(coupon.getUseThreshold())));
            });
        } else {
            throw new BusinessException(ResponseData.FAIL, "满减券码[" + couponUser.getCouponCode() + "]未能找到对应的商品");
        }
    }

    /**
     * 折扣券的优惠金额计算, 折扣券不再分摊别的券了
     */
    private void calculateDiscountForDiscount(List<CalculateDiscountRespDto> respDtoList, CalculateDiscountReqDto reqDto, CouponUser couponUser, Coupon coupon, Map<Long, List<ActivityGoods>> activityGoodsGroupMap) {
        final List<ConsumedGoodsReqDto> metGoods = this.getMetGoodsList(respDtoList, reqDto, couponUser, coupon, activityGoodsGroupMap);

        // 算出本次券码适用的商品对应的小计金额
        final BigDecimal metGoodsSubtotal = metGoods.stream().map(ConsumedGoodsReqDto::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (metGoodsSubtotal.compareTo(BigDecimal.valueOf(coupon.getUseThreshold())) >= 0) {
            metGoods.forEach(o -> {
                final BigDecimal goodsPercent = BigDecimal.valueOf(coupon.getCouponValue()).divide(BigDecimal.valueOf(100), 20, RoundingMode.HALF_UP);
                this.addCouponToRespList(respDtoList, couponUser, o, goodsPercent.multiply(o.getSubtotal()));
                o.setSubtotal(BigDecimal.ZERO);
            });
        } else {
            throw new BusinessException(ResponseData.FAIL, "折扣券码[" + couponUser.getCouponCode() + "]未能找到对应的商品");
        }
    }

}
