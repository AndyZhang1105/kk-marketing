package com.kk.marketing.web.adapter;

import com.kk.arch.dubbo.common.util.AssertUtils;
import com.kk.gateway.auth.dto.UserDto;
import com.kk.gateway.auth.remote.UserTokenService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author Zal
 */
@Component
public class TokenAdapter {

    @DubboReference
    private UserTokenService userTokenService;

    public UserDto getUserByToken(String token) {
        AssertUtils.isNotBlank(token, "参数token不能为空");
        return userTokenService.getUserByToken(token);
    }

}
