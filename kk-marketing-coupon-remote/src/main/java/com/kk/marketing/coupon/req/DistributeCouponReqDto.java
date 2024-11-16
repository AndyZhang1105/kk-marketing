package com.kk.marketing.coupon.req;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author Zal
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class DistributeCouponReqDto implements Serializable {

    public static final int MODE_TOLERANT = 0;
    public static final int MODE_STRICT = 1;

    /**
     * 租户id
     */
    @NotEmpty(message = "租户id不能为空")
    private Long tenantId;

    /**
     * 发放的会员用户id列表
     */
    @NotEmpty(message = "发放的会员用户id列表不能为空")
    @Size(min = 1, max = 1000, message = "同一批次发券不能超过1000个会员")
    private List<Long> userIdList;

    /**
     * 券列表
     */
    @NotEmpty(message = "发放的券列表不能为空")
    @Size(min = 1, max = 10, message = "同一批次发券不能超过10种券")
    private List<CouponDto> couponList;

    /**
     * 模式，0非严格模式， 1严格模式（必须全部发券成功才可以，否则发券失败）
     */
    @NotEmpty(message = "发券的模式")
    private Integer mode = MODE_TOLERANT;

    @NotEmpty(message = "来源的营销活动类型")
    private Integer sourceActivityType;

    @NotEmpty(message = "来源的营销活动Id")
    private Integer sourceActivityId;

    @NotEmpty(message = "来源的营销活动名称")
    private String sourceActivityName;

    @NotEmpty(message = "来源的门店id")
    private Long sourceStoreId;

    @NotEmpty(message = "来源的门店名称")
    private String sourceStoreName;

    @NotEmpty(message = "来源的订单编号")
    private String sourceOrderNbr;

    @NotEmpty(message = "操作人")
    private Long operatorId;

    @Data
    @Builder
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponDto implements Serializable {

        @NotNull(message = "发放的券id不能为空")
        private Long couponId;

        @NotNull(message = "发放的券数量不能为空")
        @Size(min = 1, max = 100, message = "同一批次发券的单种券不能超过100张")
        private Integer num;

    }

}
