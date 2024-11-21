package com.kk.marketing.coupon.filter;

import com.kk.marketing.coupon.conf.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Arrays;

import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

/**
 * @author Zal
 * Now is not avaliable, need to debug and let it work, todo.
 */
@Activate(group = PROVIDER)
@Slf4j
public class TenantFilter implements Filter {

    public static final String TENANT_ID = "tenantId";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long tenantId = Long.parseLong(RpcContext.getClientAttachment().getAttachment(TENANT_ID));
        if(tenantId > 0) {
            RpcContext.getServiceContext().setAttachment(TENANT_ID, tenantId);
            TenantContextHolder.setTenantId(tenantId);
        } else {
            throw new RpcException("租户id不能为空");
        }

        String methodName = invocation.getMethodName();
        Object[] arguments = invocation.getArguments();
        System.out.println("Dubbo Calling method: " + methodName);
        System.out.println("Dubbo Request arguments: " + Arrays.toString(arguments));

        Result result = invoker.invoke(invocation);

        System.out.println("Dubbo Response result: " + result.getValue());

        // 在服务调用后，从请求中提取租户 ID 信息
        tenantId = Long.parseLong(RpcContext.getServiceContext().getAttachment(TENANT_ID));
        if (tenantId > 0) {
            log.info("Tenant ID: {}", tenantId);
        }

        return result;
    }

}
