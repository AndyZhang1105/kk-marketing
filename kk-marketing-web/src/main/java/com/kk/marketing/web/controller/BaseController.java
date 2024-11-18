package com.kk.marketing.web.controller;

import com.kk.gateway.auth.dto.UserDto;
import com.kk.gateway.auth.remote.UserTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Synchronized;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static com.kk.gateway.auth.contants.AuthConstants.HEADER_TOKEN;

/**
 * @author Zal
 */
public class BaseController {

    @DubboReference(lazy = true)
    private UserTokenService userTokenService;

    private HttpServletRequest request;
    private UserDto userDto;

    @Synchronized
    protected HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            this.request = ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return this.request;
    }

    /**
     * get tenant id
     */
    protected Long getTenantId() {
        return getUser().getTenantId();
    }

    protected Long getUserId() {
        return getUser().getUserId();
    }

    @Synchronized
    protected UserDto getUser() {
        if (Objects.isNull(this.userDto)) {
            this.userDto = userTokenService.getUserByToken(getRequest().getHeader(HEADER_TOKEN));
        }
        return this.userDto;
    }

    protected String getStoreName(Long storeId) {
        return "测试门店";
    }

}
