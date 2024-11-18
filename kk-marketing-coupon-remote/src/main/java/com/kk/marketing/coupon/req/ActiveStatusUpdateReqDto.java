package com.kk.marketing.coupon.req;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * @author Zal
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ActiveStatusUpdateReqDto implements Serializable {

    @NotNull(message = "租户id不能为空")
    private Long tenantId;

    @NotNull(message = "id不能不为空")
    private Long id;

    @NotNull(message = "操作人不能不为空")
    private Long updateBy;

}
