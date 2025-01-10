package com.kk.marketing.coupon.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

/**
 * @author Zal
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeCouponUserReqDto implements Serializable {

    @Schema(description = "序号")
    @NotBlank(message = "序号不能为空")
    Integer number;

    @Schema(description = "券码")
    @NotBlank(message = "券码不能为空")
    String couponCode;

}
