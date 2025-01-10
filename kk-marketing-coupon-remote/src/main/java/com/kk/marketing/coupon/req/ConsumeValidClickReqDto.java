package com.kk.marketing.coupon.req;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ConsumeValidClickReqDto extends ConsumeQueryReqDto implements Serializable {

    @Schema(description = "用户id")
    @NotNull(message = "用户id不能为空")
    private List<ConsumeCouponUserReqDto> couponUserReqList;

}
