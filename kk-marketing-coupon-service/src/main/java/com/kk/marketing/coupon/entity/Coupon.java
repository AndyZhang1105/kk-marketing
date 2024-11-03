package com.kk.marketing.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("t_coupon")
public class Coupon extends BaseEntity implements Serializable {

	@TableId
	@TableField("coupon_id")
	private Long couponId;

	@TableField("coupon_name")
	private String couponName;

}
