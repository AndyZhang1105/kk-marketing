package com.kk.marketing.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Zal
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_coupon_data")
public class CouponData extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 券id
     */
    private Long couponId;

    /**
     * 已发放的用户券数量
     */
    private Integer numberDistributed;

    /**
     * 已核销的用户券数量
     */
    private Integer numberConsumed;

    /**
     * 已核销的订单数量
     */
    private Integer orderNumberConsumed;

    /**
     * 已核销的订单金额
     */
    private Integer orderTotalConsumed;

}
