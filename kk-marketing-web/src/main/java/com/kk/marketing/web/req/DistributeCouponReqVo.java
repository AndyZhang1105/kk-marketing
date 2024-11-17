package com.kk.marketing.web.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Zal
 */
@Data
public class DistributeCouponReqVo implements Serializable {

    @NotEmpty(message = "同一批次发券不能超过1000个会员")
    @Size(min = 1, max = 1000, message = "同一批次发券不能超过1000个会员")
    private List<Long> userIdList;

    @Size(min = 1, max = 10, message = "同一批次发券不能超过10种券")
    private List<DistributeCouponDetailReqVo> couponList;

    @NotNull(message = "发券的门店id不能为空")
    private Long sourceStoreId;

}
