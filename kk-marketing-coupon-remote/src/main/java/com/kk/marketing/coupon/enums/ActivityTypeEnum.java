package com.kk.marketing.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zal
 */
@AllArgsConstructor
@Getter
public enum ActivityTypeEnum {

    COUPON(1, "优惠券"),
    UNKNOWN(99, "未知");

    private int code;
    private String desc;

    /**
     * 从code值获取对应的枚举
     */
    public static ActivityTypeEnum getByCode(int code) {
        for (ActivityTypeEnum typeEnum : ActivityTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum;
            }
        }
        return null;
    }

}
