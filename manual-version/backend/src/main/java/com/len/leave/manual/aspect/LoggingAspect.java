package com.len.leave.manual.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Logging Aspect
 * 
 * Provides structured logging for all service and controller methods.
 * This is part of the enterprise-grade logging implementation.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (Persero)
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut for all service methods
     */
    @Pointcut("execution(* com.len.leave.manual.service..*(..))")
    public void serviceMethods() {}

    /**
     * Pointcut for all controller methods
     */
    @Pointcut("execution(* com.len.leave.manual.controller..*(..))")
    public void controllerMethods() {}

    /**
     * Log before executing service methods
     */
    @Before("serviceMethods()")
    public void logBeforeService(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        log.debug("ENTER: {}.{}() with arguments: {}", className, methodName, Arrays.toString(args));
    }

    /**
     * Log after executing service methods
     */
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterService(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        log.debug("EXIT: {}.{}() with result: {}", className, methodName, 
                result != null ? result.getClass().getSimpleName() : "null");
    }

    /**
     * Log exceptions in service methods
     */
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
    public void logServiceException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        log.error("EXCEPTION in {}.{}(): {}", className, methodName, exception.getMessage(), exception);
    }

    /**
     * Log controller requests
     */
    @Before("controllerMethods()")
    public void logControllerRequest(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        log.info("REQUEST: {}.{}() with arguments: {}", className, methodName, Arrays.toString(args));
    }

    /**
     * Log controller responses
     */
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logControllerResponse(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        log.info("RESPONSE: {}.{}() completed successfully", className, methodName);
    }
}
