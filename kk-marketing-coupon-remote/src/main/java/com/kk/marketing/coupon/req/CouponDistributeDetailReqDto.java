package com.kk.marketing.coupon.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CouponDistributeDetailReqDto implements Serializable {

    @NotNull(message = "发放的券id不能为空")
    private Long couponId;

    @NotNull(message = "发放的券数量不能为空")
    @Size(min = 1, max = 100, message = "同一批次发券的单种券不能超过100张")
    private Integer num;

}
