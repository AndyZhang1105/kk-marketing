package com.kk.marketing.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 券可用时间类型，0固定时段，1自领取后按天算
 * @author Zal
 */
@AllArgsConstructor
@Getter
public enum UsableTimeTypeEnum {

    FIXED(0, "固定时间段"),
    FLOAT(1, "自领取后按天算");

    private int code;
    private String desc;

    /**
     * 从code值获取对应的枚举
     */
    public static UsableTimeTypeEnum getByCode(int code) {
        for (UsableTimeTypeEnum typeEnum : UsableTimeTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum;
            }
        }
        return null;
    }
}
