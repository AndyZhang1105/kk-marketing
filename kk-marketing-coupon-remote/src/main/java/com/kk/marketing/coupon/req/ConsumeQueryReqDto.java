package com.kk.marketing.coupon.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author Zal
 */
@Data
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeQueryReqDto implements Serializable {

    @Schema(description = "用户id")
    @NotNull(message = "用户id不能为空")
    Long userId;

    @Schema(description = "门店id")
    @NotNull(message = "门店id不能为空")
    Long storeId;

    @Schema(description = "使用平台不能为空, 0小程序，1线下门店")
    @NotNull(message = "使用平台不能为空")
    Integer usePlatform;

    @Schema(description = "商品列表")
    @NotEmpty(message = "商品列表不能为空")
    List<ConsumedGoodsReqDto> goodsList;

}
