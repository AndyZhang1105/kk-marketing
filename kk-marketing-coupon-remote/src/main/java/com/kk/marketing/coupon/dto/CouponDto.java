package com.kk.marketing.coupon.dto;

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
public
class CouponDto extends BaseDto implements Serializable {

    private Long id;
    private String couponName;

}
