package com.kk.marketing.coupon.resp;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ConsumeQueryRespDto implements Serializable {

    @Schema(description = "推荐列表")
    List<ConsumeQueryCouponUserRespDto> recommendList;

    @Schema(description = "可用列表")
    List<ConsumeQueryCouponUserRespDto> usableList;

    @Schema(description = "不可用列表")
    List<ConsumeQueryCouponUserRespDto> unusableList;

}
