package com.kk.marketing.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity base class
 * @author Zal
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class BaseVo implements Serializable {

    private Long tenantId;

    // private Integer deleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    private Long createBy;

    private Long updateBy;

}
