package com.kk.marketing.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 券模板信息表
 *
 * @author Zal
 * @since 2024-11-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@TableName("t_coupon")
@Schema(name = "Coupon", description = "券模板信息表")
public class Coupon extends BaseEntity {

    @Schema(description = "自增主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "券名称")
    private String couponName;

    @Schema(description = "使用类型，0电子券，1纸质券")
    private Integer useType;

    @Schema(description = "券类型，0")
    private Integer couponType;

    @Schema(description = "券图片url")
    private String imageUrl;

    @Schema(description = "最低消费，如果为0，则表示无门槛")
    private Integer useThreshold;

    @Schema(description = "券的折扣或减免的额度")
    private Integer couponValue;

    @Schema(description = "可使用平台列表，0小程序，1线下门店，逗号隔开")
    private String usePlatforms;

    @Schema(description = "使用范围，0单品，1品类，2品牌，3全场")
    private Integer useScope;

    @Schema(description = "可用门店类型，0全部门店，1部分门店")
    private Boolean storeType;

    @Schema(description = "券可用时间类型，0固定时段，1自领取后按天算")
    private Integer usableTimeType;

    @Schema(description = "券可用结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableFixedStart;

    @Schema(description = "券可用开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableFixedEnd;

    @Schema(description = "自领取后按x天算，当天生效填写0")
    private Integer usableFlexFrom;

    @Schema(description = "自领取后按几天算，有效期x天")
    private Integer usableFlexDay;

    @Schema(description = "可使用时段类型，0全部时段，1部分时段")
    private Integer periodType;

    @Schema(description = "可使用星期列表，逗号隔开，周日是7")
    private String periodWeeks;

    @Schema(description = "可使用的时间段列表，逗号隔开")
    private String periodTimeSegments;

    @Schema(description = "活动可与优惠券共享优惠，逗号隔开")
    private String shareableActivities;

    @Schema(description = "是否可转赠，1可，0不可")
    private Integer isTransferable;

    @Schema(description = "券使用说明及描述")
    private String useInstruction;

    @Schema(description = "券库存总数量")
    private Integer numberTotal;

    @Schema(description = "券库存是否告警开启，0不告警，1告警")
    private Boolean stockWarnEnabled;

    @Schema(description = "库存安全数量，低于此值将发送告警短信")
    private Integer stockSafeQuantity;

    @Schema(description = "库存低于安全数量后，告警发送的手机号")
    private String stockMsgPhone;

    @Schema(description = "领取限制类型，0-不限制, 1-每天,2-每周,3-每月,4-每年")
    private Integer distributeLimitType;

    @Schema(description = "领取限制类量")
    private Integer distributeLimitNumber;

    @Schema(description = "小程序二维码扫码领取限制数量")
    private Integer distributeScanLimitNumber;

    @Schema(description = "启用状态，0未启用，1已启用")
    private Integer activeStatus;

    @Schema(description = "核销限制类型，0-不限制, 1-每天,2-每周,3-每月,4-每年")
    private Integer consumePeriodLimitType;

    @Schema(description = "核销限制数量")
    private Integer consumePeriodLimitNumber;

    @Schema(description = "订单最大可使用此券张数, 1-20")
    private Integer consumeOrderUseLimit;

    @Schema(description = "券叠加类型:0-不叠加,1-不能叠加部分优惠券,2-可与所有优惠券叠加")
    private Integer consumeStackingUseType;

    @Schema(description = "当订单全额退款时，已核销券是否置为未使用，0不，1是")
    private Integer recycleAfterRefund;

    @Schema(description = "券的简单描述")
    private String shortDesc;
}
