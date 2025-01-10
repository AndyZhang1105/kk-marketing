package com.kk.marketing.coupon.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
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
public class ConsumeQueryCouponUserRespDto implements Serializable {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "会员userid")
    private Long userId;

    @Schema(description = "优惠券id")
    private Long couponId;

    @Schema(description = "券码")
    private String couponCode;

    @Schema(description = "可使用的开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableStartTime;

    @Schema(description = "可使用的终止日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableEndTime;

    @Schema(description = "状态：未使用(0),已使用(1),转赠中(2),已转赠(3),已关闭(4),锁定待核销(5)")
    private Integer status;

    @Schema(description = "此券优惠金额")
    private BigDecimal discount;

    @Schema(description = "是否可用于核销")
    private Boolean isUsable;

    @Schema(description = "不可用的原因")
    private String unusableMsg;

    @Schema(description = "最低消费，如果为0，则表示无门槛")
    private Integer useThreshold;

    public static Comparator<ConsumeQueryCouponUserRespDto> customCompare() {
        return Comparator.comparing(ConsumeQueryCouponUserRespDto::getDiscount, Comparator.reverseOrder())
                         .thenComparing(ConsumeQueryCouponUserRespDto::getUsableEndTime);
    }
}

