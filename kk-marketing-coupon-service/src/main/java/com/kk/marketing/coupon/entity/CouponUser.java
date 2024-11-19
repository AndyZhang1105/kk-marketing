package com.kk.marketing.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 用户优惠券信息表
 *
 * @author Zal
 * @since 2024-11-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_coupon_user")
@Schema(name = "CouponUser", description = "用户优惠券信息表")
public class CouponUser extends BaseEntity {

    @Schema(description = "自增主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "会员userid")
    private Long userId;

    @Schema(description = "优惠券id")
    private Long couponId;

    @Schema(description = "券码")
    private String couponCode;

    @Schema(description = "来源活动类型")
    private Integer sourceActivityType;

    @Schema(description = "来源活动ID")
    private Integer sourceActivityId;

    @Schema(description = "来源活动名称")
    private String sourceActivityName;

    @Schema(description = "发券来源门店id")
    private Long sourceStoreId;

    @Schema(description = "发券来源门店名称")
    private String sourceStoreName;

    @Schema(description = "来源订单号")
    private String sourceOrderNbr;

    @Schema(description = "可使用的开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableStartTime;

    @Schema(description = "可使用的终止日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableEndTime;

    @Schema(description = "状态：未使用(0),已使用(1),转赠中(2),已转赠(3),已关闭(4),锁定待核销(5)")
    private Integer status;

    @Schema(description = "核销时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date consumeTime;

    @Schema(description = "核销的门店id")
    private Long consumeStoreId;

    @Schema(description = "核销的门店名称")
    private String consumeStoreName;

    @Schema(description = "核销平台，未知0,微商城1,前端收银2,券核销员3")
    private Integer consumePlatform;

    @Schema(description = "实际优惠券优惠金额")
    private Integer consumeDiscountAmount;

    @Schema(description = "核销的订单号")
    private String consumeOrderNbr;

    @Schema(description = "核销操作人")
    private Long consumeOperatorId;

    @Schema(description = "核销订单的总金额")
    private Long consumeOrderTotal;

    @Schema(description = "扩展json")
    private String extJson;
}
