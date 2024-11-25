package com.kk.marketing.coupon.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
public class ActivityGoodsAddReqDto {

    @ApiModelProperty(value = "活动类型，1优惠券，2多件促销", required = true)
    @NotNull(message = "活动类型不能为空")
    private Integer activityType;

    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long activityId;

    @ApiModelProperty(value = "商品范围，0单品，1品类，2品牌", required = true)
    @NotNull(message = "商品范围不能为空")
    private Integer goodsScope;

    @ApiModelProperty(value = "根据使用范围，依次是商品upc, 品类code ,品牌code", required = true)
    @NotNull(message = "商品值不能为空")
    private String goodsValue;

    @ApiModelProperty(value = "商品活动价格", required = true)
    @NotNull(message = "商品活动价格不能为空")
    private Integer activityPrice;

    @ApiModelProperty(value = "商品活动库存", required = true)
    @NotNull(message = "商品活动库存不能为空")
    private Integer activityStock;

    @ApiModelProperty(value = "商品活动json")
    private String activityJson;

    @ApiModelProperty(value = "0不适用, 适用1", required = true)
    @NotNull(message = "商品是否适用不能为空")
    private Integer isAvailable;

}
