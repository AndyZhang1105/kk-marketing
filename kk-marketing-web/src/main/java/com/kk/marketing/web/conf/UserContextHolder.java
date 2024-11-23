package com.kk.marketing.web.conf;

import com.kk.gateway.auth.dto.UserDto;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Zal
 * current user info context holder, like operator info
 */
@Component
@Slf4j
public class UserContextHolder {

    private static final ThreadLocal<UserDto> CONTEXT = new ThreadLocal<>();

    @Synchronized
    public static void setUser(UserDto user) {
        Optional.ofNullable(user).ifPresent(CONTEXT::set);
    }

    public static UserDto getUser() {
        return CONTEXT.get();
    }

    public static Long getTenantId() {
        return Optional.ofNullable(getUser()).map(UserDto::getTenantId).orElse(0L);
    }

    public static Long getUserId() {
        return Optional.ofNullable(getUser()).map(UserDto::getUserId).orElse(0L);
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
