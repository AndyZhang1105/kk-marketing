package com.kk.marketing.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zal
 */
@AllArgsConstructor
@Getter
public enum ActiveStatusEnum {

    CLOSE(0, "未启用"),
    OPEN(1, "已启用");

    private int code;
    private String desc;

    /**
     * 从code值获取对应的枚举
     */
    public static ActiveStatusEnum getByCode(int code) {
        for (ActiveStatusEnum typeEnum : ActiveStatusEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum;
            }
        }
        return null;
    }

}
