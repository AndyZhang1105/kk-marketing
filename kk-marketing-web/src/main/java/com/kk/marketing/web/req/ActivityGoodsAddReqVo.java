package com.kk.marketing.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author Zal
 */
@Data
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "活动适用或不适用的商品信息")
public class ActivityGoodsAddReqVo implements Serializable {

    @Schema(description = "活动类型，1优惠券，2多件促销")
    @NotNull(message = "活动类型不能为空")
    private Integer activityType;

    @Schema(description = "活动id")
    @NotNull(message = "活动id不能为空")
    private Long activityId;

    @Schema(description = "商品范围，0单品，1品类，2品牌")
    @NotNull(message = "商品范围不能为空")
    private Integer goodsScope;

    @Schema(description = "根据使用范围，依次是商品upc, 品类code ,品牌code")
    @NotNull(message = "商品值不能为空")
    private String goodsValue;

    @Schema(description = "商品活动价格")
    @NotNull(message = "商品活动价格不能为空")
    private Integer activityPrice;

    @Schema(description = "商品活动库存")
    @NotNull(message = "商品活动库存不能为空")
    private Integer activityStock;

    @Schema(description = "商品活动json")
    private String activityJson;

    @Schema(description = "0不适用, 适用1")
    @NotNull(message = "商品是否适用不能为空")
    private Integer isAvailable;

}
