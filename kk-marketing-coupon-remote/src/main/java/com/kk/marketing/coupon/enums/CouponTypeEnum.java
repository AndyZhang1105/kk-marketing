package com.kk.marketing.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zal
 */
@AllArgsConstructor
@Getter
public enum CouponTypeEnum {

    FULL(1, "满减券"),
    DISCOUNT(2, "折扣券"),
    GIFT(3, "兑换券"),
    FIXED(4, "定额券"),
    UNKNOWN(99, "未知");

    private final int code;
    private final String desc;

    /**
     * 从code值获取对应的枚举
     */
    public static CouponTypeEnum getByCode(int code) {
        for (CouponTypeEnum typeEnum : CouponTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum;
            }
        }
        return null;
    }

}
