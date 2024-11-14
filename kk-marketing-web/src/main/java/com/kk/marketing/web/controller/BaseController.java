package com.kk.marketing.web.controller;

import com.kk.gateway.auth.dto.UserDto;
import com.kk.gateway.auth.remote.UserTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Synchronized;
import org.apache.dubbo.config.annotation.DubboReference;

import java.util.Objects;

import static com.kk.gateway.auth.contants.AuthConstants.HEADER_TOKEN;

/**
 * @author Zal
 */
public class BaseController {

    @DubboReference(lazy = true)
    private UserTokenService userTokenService;

    private UserDto userDto;

    /**
     * get tenant id
     */
    protected Long getTenantId(HttpServletRequest request) {
        return getUser(request).getTenantId();
    }

    protected Long getUserId(HttpServletRequest request) {
        return getUser(request).getUserId();
    }

    @Synchronized
    protected UserDto getUser(HttpServletRequest request) {
        if (Objects.isNull(this.userDto)) {
            this.userDto = userTokenService.getUserByToken(request.getHeader(HEADER_TOKEN));
        }
        return this.userDto;
    }

}
