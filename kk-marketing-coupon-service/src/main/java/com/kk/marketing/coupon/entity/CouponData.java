package com.kk.marketing.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 券变化信息表
 *
 * @author Zal
 * @since 2024-11-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@TableName("t_coupon_data")
@Schema(name = "CouponData", description = "券变化信息表")
public class CouponData extends BaseEntity {

    @Schema(description = "自增主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "券id")
    private Long couponId;

    @Schema(description = "已领取的用户券数量")
    private Integer numberDistributed;

    @Schema(description = "已核销的用户券数量")
    private Integer numberConsumed;

    @Schema(description = "已核销的订单数量")
    private Integer orderNumberConsumed;

    @Schema(description = "已核销的订单金额")
    private Integer orderTotalConsumed;
}
