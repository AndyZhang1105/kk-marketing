package com.kk.marketing.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 活动相关的商品信息
 *
 * @author Zal
 * @since 2024-11-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@TableName("t_activity_goods")
@Schema(name = "ActivityGoods", description = "活动相关的商品信息")
public class ActivityGoods extends BaseEntity {

    @Schema(description = "自增主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "活动类型，1优惠券，2多件促销")
    private Integer activityType;

    @Schema(description = "活动id")
    private Long activityId;

    @Schema(description = "商品范围，0单品，1品类，2品牌")
    private Integer goodsScope;

    @Schema(description = "根据使用范围，依次是商品upc, 品类code ,品牌code")
    private String goodsValue;

    @Schema(description = "商品活动价格")
    private Integer activityPrice;

    @Schema(description = "商品活动库存")
    private Integer activityStock;

    @Schema(description = "商品活动json")
    private String activityJson;

    @Schema(description = "0不可用, 可用的1")
    private Integer isAvailable;
}
