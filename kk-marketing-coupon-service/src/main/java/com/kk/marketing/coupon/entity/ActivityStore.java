package com.kk.marketing.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kk.marketing.coupon.entity.BaseEntity;
import com.kk.marketing.coupon.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 适用门店信息表
 *
 * @author Zal
 * @since 2024-12-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_activity_store")
@Schema(name = "ActivityStore", description = "适用门店信息表")
public class ActivityStore extends BaseEntity {

    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "活动类型")
    private Integer activityType;

    @Schema(description = "活动id")
    private Long activityId;

    @Schema(description = "门店编号")
    private Long storeId;

    @Schema(description = "门店名称")
    private String storeName;
}
