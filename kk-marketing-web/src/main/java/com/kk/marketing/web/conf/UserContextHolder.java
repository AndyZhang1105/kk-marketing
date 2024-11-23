package com.kk.marketing.web.conf;

import com.kk.gateway.auth.dto.UserDto;
import com.kk.marketing.web.adapter.TokenAdapter;
import com.kk.marketing.web.util.RequestUtils;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.kk.arch.common.constants.CommonConstants.HEADER_TOKEN;

/**
 * @author Zal
 * current user info context holder, like operator info
 */
@Component
@Slf4j
public class UserContextHolder {

    private static final ThreadLocal<UserDto> CONTEXT = new ThreadLocal<>();

    /**
     * set user info
     */
    @Synchronized
    public static void setUser() {
        if(!Objects.isNull(CONTEXT.get())) {
            return;
        }

        TokenAdapter tokenAdapter = ApplicationContextHelper.getBean(TokenAdapter.class);
        if(tokenAdapter != null) {
            UserDto userDto = tokenAdapter.getUserByToken(RequestUtils.getHeader(HEADER_TOKEN));
            Optional.ofNullable(userDto).ifPresent(CONTEXT::set);
        }
    }

    /**
     * 获取当前租户信息
     */
    public static UserDto getUser() {
        if(Objects.isNull(CONTEXT.get())) {
            setUser();
        }
        return CONTEXT.get();
    }

    public static Long getTenantId() {
        return getUser().getTenantId();
    }

    public static Long getTenantIdIfPresent() {
        return Optional.ofNullable(CONTEXT.get()).map(UserDto::getTenantId).orElse(null);
    }

    public static Long getUserId() {
        return getUser().getUserId();
    }

    /**
     * 清除
     */
    public static void clear() {
        CONTEXT.remove();
    }

}
