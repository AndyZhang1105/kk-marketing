package com.kk.marketing.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zal
 */
@AllArgsConstructor
@Getter
public enum UseScopeEnum {

    SINGLE(0, "单品"),
    CATEGORY(1, "品类"),
    BRAND(2, "品牌"),
    WHOLE(3, "全场"),
    UNKNOWN(99, "未知");

    private final int code;
    private final String desc;

    /**
     * 从code值获取对应的枚举
     */
    public static UseScopeEnum getByCode(int code) {
        for (UseScopeEnum typeEnum : UseScopeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum;
            }
        }
        return null;
    }

}
