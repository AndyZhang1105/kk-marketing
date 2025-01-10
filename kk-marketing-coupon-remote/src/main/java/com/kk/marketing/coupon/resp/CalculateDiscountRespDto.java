package com.kk.marketing.coupon.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Zal
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CalculateDiscountRespDto implements Serializable {

    @Schema(description = "商品序号")
    Integer number;

    @Schema(description = "商品upc")
    private String upc;

    @Schema(description = "券码")
    String couponCode;

    @Schema(description = "券码在此商品upc的优惠金额")
    BigDecimal discount;


}
