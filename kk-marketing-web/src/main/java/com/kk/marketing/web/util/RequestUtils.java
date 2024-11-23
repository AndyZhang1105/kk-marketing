package com.kk.marketing.web.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

/**
 * @author Zal
 */
public class RequestUtils {

    /**
     * 获取 Request 对象
     */
    public static Optional<HttpServletRequest> getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return Optional.of(((ServletRequestAttributes) requestAttributes).getRequest());
        }
        return Optional.empty();
    }

    /**
     * 获取所有 Header 信息
     *
     * @return 包含所有 Header 信息的 Map
     */
    public static Map<String, String> getAllHeaders() {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = getRequest().map(HttpServletRequest::getHeaderNames).orElse(Collections.emptyEnumeration());
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = getRequest().map(o -> o.getHeader(headerName)).orElse(Strings.EMPTY);
            headers.put(headerName, headerValue);
        }
        return headers;
    }

    /**
     * 获取特定 Header 信息
     *
     * @param headerName Header 名称
     * @return Header 值
     */
    public static String getHeader(String headerName) {
        return getRequest().map(o -> o.getHeader(headerName)).orElse(Strings.EMPTY);
    }

}
