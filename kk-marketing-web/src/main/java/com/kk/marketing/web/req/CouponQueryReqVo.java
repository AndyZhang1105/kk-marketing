package com.kk.marketing.web.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Zal
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CouponQueryReqVo implements Serializable {

    @ApiModelProperty("券id")
    private Long id;

    @ApiModelProperty("券名称")
    private String couponName;

    @ApiModelProperty("券类型")
    private Integer couponType;

    @ApiModelProperty("创建时间开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @ApiModelProperty("创建时间结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

    @ApiModelProperty("启用状态, 0未启用，1已启用")
    private Integer activeStatus;

}
