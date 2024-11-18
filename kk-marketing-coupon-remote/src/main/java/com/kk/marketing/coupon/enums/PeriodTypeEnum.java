package com.kk.marketing.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zal
 * 可使用时段类型，0全部时段，1部分时段
 */
@AllArgsConstructor
@Getter
public enum PeriodTypeEnum {
    ALL(0, "全部时段"),
    PART(1, "部分时段");

    private int code;
    private String desc;

    /**
     * 从code值获取对应的枚举
     */
    public static PeriodTypeEnum getByCode(int code) {
        for (PeriodTypeEnum typeEnum : PeriodTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum;
            }
        }
        return null;
    }

}
