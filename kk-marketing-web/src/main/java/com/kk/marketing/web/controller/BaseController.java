package com.kk.marketing.web.controller;

import com.kk.marketing.web.conf.UserContextHolder;


/**
 * @author Zal
 */
public class BaseController {

    /**
     * get tenant id
     */
    protected Long getTenantId() {
        return UserContextHolder.getTenantId();
    }

    protected Long getUserId() {
        return UserContextHolder.getUserId();
    }

    protected String getStoreName(Long storeId) {
        return "测试门店";
    }

}
