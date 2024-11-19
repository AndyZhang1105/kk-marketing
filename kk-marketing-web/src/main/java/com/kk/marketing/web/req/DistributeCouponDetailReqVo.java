package com.kk.marketing.web.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author Zal
 */
@Data
public class DistributeCouponDetailReqVo {

    @NotNull(message = "发放的券id不能为空")
    @Range(min = 1, max = Integer.MAX_VALUE)
    private Long couponId;

    @NotNull(message = "单种券发放数量不能为空")
    @Range(min = 1, max = 100, message = "单种券发放数量必须是1-100")
    private Integer num;

}
