package com.kk.marketing.coupon.conf;

import org.springframework.stereotype.Component;

/**
 * @author Zal
 */
@Component
public class TenantContextHolder {

    /**
     * InheritableThreadLocal 子线程也可以访问到这个值
     */
    private static final InheritableThreadLocal<Long> CONTEXT = new InheritableThreadLocal<>();

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
