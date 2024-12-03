package com.kk.marketing.coupon.req;

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

    private Long tenantId;

}
