package com.ebay.park.util;

import org.aspectj.lang.ProceedingJoinPoint;


public interface StatisticsAspect {
    public Object logCall(ProceedingJoinPoint pjp) throws Throwable;

}
