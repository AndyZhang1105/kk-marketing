package com.kk.marketing.coupon.conf;

import com.alibaba.fastjson.JSON;
import com.kk.arch.exception.BusinessException;
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

    @Around("execution(* com.kk.marketing.coupon.remote.impl.*RemoteImpl.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Method: {}, params: {}", joinPoint.getSignature(), JSON.toJSONString(joinPoint.getArgs()));

        long start = System.currentTimeMillis();

        // 执行目标方法
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                logger.info("Method: {}, business exception: {}", joinPoint.getSignature(), e.getMessage());
            } else {
                logger.error("Method: {}, error: {}, stack: {}", joinPoint.getSignature(), e.getMessage(), JSON.toJSONString(e.getStackTrace()));
            }
            return ResponseUtils.fail(e.getMessage());
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            logger.info("Method: {} executed in {} ms", joinPoint.getSignature(), elapsedTime);
        }
    }
}