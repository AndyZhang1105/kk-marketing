package com.kk.marketing.coupon.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class ConsumeReqDto implements Serializable {

    @Schema(description = "用户id")
    @NotNull(message = "用户id不能为空")
    Long userId;

    @Schema(description = "券码列表")
    @NotEmpty(message = "券码列表不能为空")
    List<String> couponCodeList;
    
}
