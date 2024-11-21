package com.kk.marketing.coupon.conf;

import org.springframework.stereotype.Component;

/**
 * @author Zal
 */
@Component
public class TenantContextHolder {

    private static final ThreadLocal<Long> CONTEXT = new ThreadLocal<>();

    /**
     * 设置租户ID
     */
    public static void setTenantId(Long tenantId) {
        CONTEXT.set(tenantId);
    }

    /**
     * 获取当前租户ID
     */
    public static Long getTenantId() {
        return CONTEXT.get();
    }

    /**
     * 清除租户ID
     */
    public static void clear() {
        CONTEXT.remove();
    }

}
