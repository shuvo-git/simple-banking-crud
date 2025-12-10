package com.jobayed.banking.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(com.jobayed.banking.controllers.endpoint..*)")
    public void controllerMethods() {
    }

    @Pointcut("within(com.jobayed.banking.service..*)")
    public void serviceMethods() {
    }

    @Before("controllerMethods() || serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {}.{} with arguments: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerMethods() || serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Exiting method: {}.{} with result: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result);
    }
}
