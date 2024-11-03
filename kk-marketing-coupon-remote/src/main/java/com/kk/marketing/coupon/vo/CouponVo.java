package com.kk.marketing.coupon.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Builder
public class CouponVo implements Serializable {

	public Long couponId;

}
