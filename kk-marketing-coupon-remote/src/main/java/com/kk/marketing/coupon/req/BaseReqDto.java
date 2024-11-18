package com.kk.marketing.coupon.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Entity base class
 *
 * @author Zal
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseReqDto implements Serializable {

    @NotNull(message = "租户id不能为空")
    private Long tenantId;

}
