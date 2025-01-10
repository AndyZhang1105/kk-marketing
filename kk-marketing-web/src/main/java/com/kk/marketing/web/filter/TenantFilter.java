package com.kk.marketing.web.filter;

import com.kk.marketing.web.conf.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Arrays;

import static com.kk.arch.dubbo.common.constant.CommonConstants.TENANT_ID;
import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

/**
 * @author Zal
 * It should add META-INF.dubbo folder and create a file named after org.apache.dubbo.rpc.Filter, which define this fitler winthin it.
 * then add dubbo.provider.filter=tenantFilter to application.properties.
 */
@Activate(group = {PROVIDER, CONSUMER})
@Slf4j
public class TenantFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(TENANT_ID, UserContextHolder.getTenantId());

        String methodName = invocation.getMethodName();
        Object[] arguments = invocation.getArguments();
        log.info("Dubbo Calling method: {}, args: {}, tenantId: {}", methodName, Arrays.toString(arguments), UserContextHolder.getTenantId());

        Result result = invoker.invoke(invocation);

        log.info("Dubbo Response result: {}, tenantId: {}", result.getValue(), UserContextHolder.getTenantId());

        return result;
    }

}
