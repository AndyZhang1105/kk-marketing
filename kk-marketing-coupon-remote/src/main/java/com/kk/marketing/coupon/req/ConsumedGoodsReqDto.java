package com.kk.marketing.coupon.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ConsumedGoodsReqDto implements Serializable {

    @Schema(description = "序号")
    Integer number;

    @Schema(description = "商品upc")
    @NotBlank(message = "商品upc不能为空")
    String upc;

    @Schema(description = "商品数量")
    @NotNull(message = "商品数量不能为空")
    @DecimalMin(value = "0.01", inclusive = true, message = "商品数量不能少于0.01")
    @DecimalMax(value = "9999.99", inclusive = true, message = "商品数量不能多于9999.99")
    BigDecimal quantity;

    @Schema(description = "商品单价")
    @NotNull(message = "商品单价不能为空")
    @DecimalMin(value = "0.01", inclusive = true, message = "商品单价不能低于0.01")
    @DecimalMax(value = "999999.99", inclusive = true, message = "商品单价不能大于99999.99")
    BigDecimal price;

    @Schema(description = "商品金额小计")
    @NotNull(message = "商品金额小计不能为空")
    @DecimalMin(value = "0.01", inclusive = true, message = "商品金额小计不能低于0.01")
    @DecimalMax(value = "999999.99", inclusive = true, message = "商品金额小计不能大于999999.99")
    BigDecimal subtotal;

    @Schema(description = "商品分类编码")
    String categoryCode;

    @Schema(description = "参与的活动类型")
    Integer activityType;

    public BigDecimal getSubtotal() {
        return subtotal != null ? subtotal : quantity.multiply(price);
    }

}
