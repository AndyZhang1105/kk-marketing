package com.kk.marketing.coupon.resp;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
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
public class DistributeCouponUserRespDto implements Serializable {

    private Long id;

    private Long userId;
    private Long couponId;
    private String couponCode;

    private Integer sourceActivityType;
    private Integer sourceActivityId;
    private String sourceActivityName;
    private Long sourceStoreId;
    private String sourceStoreName;
    private String sourceOrderNbr;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usableEndTime;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date consumeTime;

    private String extJson;

}
