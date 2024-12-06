package com.opensource.cloudnest.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.stereotype.Component;

import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(TransactionLoggingAspect.class);

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("Transaction started: {}", methodName);
        try {
            Object result = joinPoint.proceed();
            logger.info("Transaction committed: {}", methodName);
            return result;
        } catch (Exception e) {
            logger.error("Transaction rolled back: {} due to {}", methodName, e.getMessage());
            throw e;
        }
    }
}
