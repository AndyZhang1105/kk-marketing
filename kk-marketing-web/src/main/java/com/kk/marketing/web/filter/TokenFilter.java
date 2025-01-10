package com.kk.marketing.web.filter;

import com.kk.gateway.auth.dto.UserDto;
import com.kk.gateway.auth.remote.UserTokenService;
import com.kk.marketing.web.conf.UserContextHolder;
import io.vavr.control.Try;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.kk.arch.dubbo.common.constant.CommonConstants.HEADER_TOKEN;


/**
 * @author Zal
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

    @DubboReference
    private UserTokenService userTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 跳过生成文档的路径
        String path = request.getServletPath();
        if (path.startsWith("/doc.html") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui/") || path.startsWith("/webjars/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(HEADER_TOKEN);

        if (token == null || token.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token is missing or invalid");
            return;
        }

        Try<UserDto> user = Try.of(() -> userTokenService.getUserByToken(token));
        if (user.isFailure() || user.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token is invalid");
            return;
        }

        UserContextHolder.setUser(user.get());
        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear();
        }
    }

}
