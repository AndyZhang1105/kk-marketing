package com.kk.marketing.coupon.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto implements Serializable {

	private Long couponId;

	private String couponName;

}
