package com.kk.marketing.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zal
 */
@AllArgsConstructor
@Getter
public enum StoreTypeEnum {

    ALL(0, "全部门店"),
    PART(1, "部分门店");

    private final int code;
    private final String desc;

    /**
     * 从code值获取对应的枚举
     */
    public static StoreTypeEnum getByCode(int code) {
        for (StoreTypeEnum typeEnum : StoreTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum;
            }
        }
        return null;
    }

}
