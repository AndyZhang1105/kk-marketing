package com.kk.marketing.coupon.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.collect.Sets;
import com.kk.arch.common.exception.BusinessException;
import com.kk.arch.common.util.JsonUtils;
import com.kk.arch.common.util.ResponseUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Zal
 */
@Aspect
@Component
@Slf4j
public class RemoteImplAspect {

    private final Validator validator;

    public RemoteImplAspect() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Around("execution(* com.kk.marketing.coupon.remote.impl.*RemoteImpl.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. manual to do validation
        Set<ConstraintViolation<Object>> violations = Sets.newHashSet();
        Arrays.stream(joinPoint.getArgs()).forEach(o -> {
            violations.addAll(validator.validate(o));
        });
        if (!violations.isEmpty()) {
            // throw new ConstraintViolationException(violations);
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<?> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseUtils.fail(JsonUtils.toJsonString(errors));
        }

        // 2. do business logic
        log.info("Method: {}, params: {}", joinPoint.getSignature(), JSON.toJSONString(joinPoint.getArgs()));
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                log.info("Method: {}, business exception: {}", joinPoint.getSignature(), e.getMessage());
            } else {
                log.error("Method: {}, error: {}, stack: {}", joinPoint.getSignature(), e.getMessage(), JSON.toJSONString(e.getStackTrace()));
            }
            return ResponseUtils.fail(e.getMessage());
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            log.info("Method: {} executed in {} ms", joinPoint.getSignature(), elapsedTime);
        }
    }
}