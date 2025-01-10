package com.kk.marketing.coupon.filter;

import com.kk.arch.dubbo.common.conf.TenantContextHolder;
import com.kk.arch.dubbo.common.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import static com.kk.arch.dubbo.common.constant.CommonConstants.TENANT_ID;
import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

/**
 * @author Zal
 * It should add META-INF.dubbo folder and create a file named after org.apache.dubbo.rpc.Filter, which define this fitler winthin it.
 * then add dubbo.provider.filter=tenantFilter to application_disabled.properties.
 */
@Activate(group = {PROVIDER, CONSUMER})
@Slf4j
public class TenantFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        AssertUtils.isNotNull(RpcContext.getContext().getAttachment(TENANT_ID), "租户id不能为空");
        AssertUtils.isTrue(Long.valueOf(RpcContext.getContext().getAttachment(TENANT_ID)) > 0, "租户id的传值非法");
        TenantContextHolder.setTenantId(Long.valueOf(RpcContext.getContext().getAttachment(TENANT_ID)));

        // String methodName = invocation.getMethodName();
        // Object[] arguments = invocation.getArguments();
        // System.out.println("Dubbo Calling method: " + methodName);
        // System.out.println("Dubbo Request arguments: " + Arrays.toString(arguments));
        Result result = invoker.invoke(invocation);
        // System.out.println("Dubbo Response result: " + result.getValue());

        TenantContextHolder.clear();

        return result;
    }

}
