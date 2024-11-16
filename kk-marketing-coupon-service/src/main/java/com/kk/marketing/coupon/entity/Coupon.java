package com.kk.marketing.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zal
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_coupon")
public class Coupon extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String couponName;
    private Integer useType;
    private Integer couponType;
    private String imageUrl;
    private Integer useThreshold;
    private Integer couponValue;
    private String usePlatforms;
    private Integer useScope;
    private Integer storeType;
    private Integer usableTimeType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableFixedStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableFixedEnd;

    private Integer usableFlexFrom;
    private Integer usableFlexDay;

    private Integer periodType;
    private String periodWeeks;
    private Integer periodTimeSegments;
    private Integer shareableActivities;
    private Integer isTransferable;
    private Integer useInstruction;
    private Integer numberTotal;
    private Integer stockWarnEnabled;
    private Integer stockSafeQuantity;
    private String stockMsgPhone;
    private Integer distributeLimitType;
    private Integer distributeLimitNumber;
    private Integer distributeScanLimitNumber;
    private Integer activeStatus;
    private Integer status;
    private Integer consumePeriodLimitType;
    private Integer consumePeriodLimitNumber;
    private Integer consumeOrderUseLimit;
    private Integer consumeStackingUseType;
    private Integer recycleAfterRefund;
    private String shortDesc;

}
