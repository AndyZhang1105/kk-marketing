package com.kk.marketing.coupon.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Zal
 */
@Data
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CouponAddReqDto extends BaseReqDto implements Serializable {

    @NotBlank(message = "券名称不能为空")
    @Size(min = 1, max = 30, message = "券名称不能超过30个字符")
    private String couponName;

    @NotNull(message = "券使用类型不能为空")
    private Integer useType;

    @NotNull(message = "券类型不能为空")
    private Integer couponType;

    private String imageUrl;

    @NotNull(message = "券使用门槛不能为空")
    private Integer useThreshold;

    @NotNull(message = "券优惠的值不能为空")
    private Integer couponValue;

    @NotBlank(message = "券使用的平台列表不能为空")
    private String usePlatforms;

    @NotNull(message = "券使用范围不能为空")
    private Integer useScope;

    @NotNull(message = "门店类型不能为空")
    private Integer storeType;

    @NotNull(message = "券可用时间类型不能为空")
    private Integer usableTimeType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableFixedStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableFixedEnd;

    private Integer usableFlexFrom;
    private Integer usableFlexDay;

    @NotNull(message = "券可用时间类型不能为空")
    private Integer periodType;

    /**
     * 可使用星期列表，逗号隔开，周日是7
     */
    private String periodWeeks;

    @NotBlank(message = "可使用的时间段列表不能为空")
    private String periodTimeSegments;

    private String shareableActivities;

    @NotNull(message = "是否可转赠不能为空")
    private Integer isTransferable;

    @NotBlank(message = "券使用说明不能为空")
    private String useInstruction;

    @NotNull(message = "券总量不能为空")
    private Integer numberTotal;

    @NotNull(message = "库存警告不能为空")
    private Integer stockWarnEnabled;

    @NotNull(message = "安全库存不能为空")
    private Integer stockSafeQuantity;

    private String stockMsgPhone;

    /**
     * 领取限制类型，0-不限制, 1-每天,2-每周,3-每月,4-每年
     */
    @NotNull(message = "领取限制类型不能为空")
    private Integer distributeLimitType;

    @NotNull(message = "领取限制数量不能为空")
    private Integer distributeLimitNumber;

    @NotNull(message = "小程序二维码扫码领取限制数量不能为空")
    private Integer distributeScanLimitNumber;

    private Integer activeStatus;
    private Integer status;

    /**
     * 核销限制类型，0-不限制, 1-每天,2-每周,3-每月,4-每年
     */
    @NotNull(message = "核销限制类型不能为空")
    private Integer consumePeriodLimitType;

    @NotNull(message = "核销限制数量不能为空")
    private Integer consumePeriodLimitNumber;

    @NotNull(message = "订单最大可使用此券张数不能为空")
    @Range(min = 1, max = 20)
    private Integer consumeOrderUseLimit;

    /**
     * 券叠加类型:0-不叠加,1-不能叠加部分优惠券,2-可与所有优惠券叠加
     */
    @NotNull(message = "券叠加类型不能为空")
    private Integer consumeStackingUseType;

    /**
     * 当订单全额退款时，已核销券是否置为未使用，0不，1是
     */
    @NotNull(message = "当订单全额退款时，已核销券是否置为未使用，不能为空")
    private Integer recycleAfterRefund;

    private String shortDesc;

    @NotNull(message = "创建人不能为空")
    private Long createBy;

    @ApiModelProperty("适用或不适用的商品列表")
    private List<ActivityGoodsAddReqDto> goodsList;

}
