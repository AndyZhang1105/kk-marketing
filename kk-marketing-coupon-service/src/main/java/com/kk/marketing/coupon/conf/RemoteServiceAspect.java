package com.kk.marketing.coupon.conf;

import com.kk.arch.util.ResponseUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Zal
 */
@Aspect
@Component
public class RemoteServiceAspect {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceAspect.class);

    @Around("execution(* com.kk.marketing.coupon.remote.impl.*RemoteServiceImpl.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // 执行目标方法
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            return ResponseUtils.fail(e.getMessage());
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            logger.info("Method: {} executed in {} ms", joinPoint.getSignature(), elapsedTime);
        }
    }
}