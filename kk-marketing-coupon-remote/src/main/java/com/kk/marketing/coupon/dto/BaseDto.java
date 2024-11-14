package com.kk.marketing.coupon.dto;

import lombok.Data;

import java.util.Date;

/**
 * Entity base class
 * @author Zal
 */
@Data
public
class BaseDto {

    private Long tenantId;

    private Integer deleted;

    private Date createTime;

    private Date updateTime;

    private Long createBy;

    private Long updateBy;
}
