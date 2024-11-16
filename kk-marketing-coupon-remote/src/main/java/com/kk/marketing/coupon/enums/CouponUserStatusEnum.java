package com.kk.marketing.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zal
 */
@AllArgsConstructor
@Getter
public enum CouponUserStatusEnum {

    UNUSED(0, "未使用"),
    USED(1, "已使用"),
    DISABLED(2, "已禁用"),
    REVOKED(3, "已废除"),
    GIFTING(4, "转赠中"),
    LOCKING(5, "锁定中"),
    UNKNOWN(99, "未知");

    private int code;
    private String desc;

    /**
     * 从code值获取对应的枚举
     */
    public static CouponUserStatusEnum getByCode(int code) {
        for (CouponUserStatusEnum typeEnum : CouponUserStatusEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum;
            }
        }
        return null;
    }

}
