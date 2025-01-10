package com.kk.marketing.coupon.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
public class CalculateDiscountReqDto extends ConsumeQueryReqDto implements Serializable {

    @Schema(description = "用户券列表")
    @NotEmpty(message = "用户券列表不能为空")
    List<ConsumeCouponUserReqDto> couponUserList;

}
