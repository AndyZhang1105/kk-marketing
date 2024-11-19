package com.kk.marketing.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * Entity base class
 *
 * @author Zal
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    @Schema(description = "租户id")
    private Long tenantId;

    @Schema(description = "逻辑删除，0否，1是")
    @TableLogic
    private Integer deleted;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Schema(description = "创建人")
    private Long createBy;

    @Schema(description = "修改人")
    private Long updateBy;
}
