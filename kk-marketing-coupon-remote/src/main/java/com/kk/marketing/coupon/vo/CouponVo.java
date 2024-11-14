package com.kk.marketing.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Zal
 */

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@SuperBuilder
@JsonIgnoreProperties
public class CouponVo extends BaseVo implements Serializable {
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
    private Date usableStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableEndTime;

    private Integer usableFixedStart;
    private Integer usableFixedDay;
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
