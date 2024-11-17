package com.kk.marketing.coupon.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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
public class CouponDistributionReqDto implements Serializable {

    public static final int MODE_TOLERANT = 0;
    public static final int MODE_STRICT = 1;

    /**
     * 租户id
     */
    @NotNull(message = "租户id不能为空")
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
    private List<CouponDistributeDetailReqDto> couponList;

    /**
     * 模式，0非严格模式， 1严格模式（必须全部发券成功才可以，否则发券失败）
     */
    @NotNull(message = "发券的模式")
    private Integer mode = MODE_TOLERANT;

    @NotNull(message = "来源的营销活动类型")
    private Integer sourceActivityType;

    @NotNull(message = "来源的营销活动Id")
    private Integer sourceActivityId;

    @NotNull(message = "来源的营销活动名称")
    private String sourceActivityName;

    @NotNull(message = "来源的门店id")
    private Long sourceStoreId;

    @NotNull(message = "来源的门店名称")
    private String sourceStoreName;

    @NotNull(message = "来源的订单编号")
    private String sourceOrderNbr;

    @NotNull(message = "操作人")
    private Long operatorId;


}
