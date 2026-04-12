package com.zivyou.learntimerangecrud.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CallTimeAspect {
    @Pointcut("execution(* com.zivyou.learntimerangecrud.service.RunwayTimeRangeService.*(..))")
    public void pointCutMethod() {

    }

    @Around("pointCutMethod()")
    public Object countCallTime(ProceedingJoinPoint pj) throws Throwable {
        long start = System.currentTimeMillis();
        var object = pj.proceed();
        long end = System.currentTimeMillis();
        long duration = end - start;
        var functionName = pj.getSignature().getName();
        System.out.printf("函数%s执行耗时: %d\n", functionName, duration);
        return object;
    }
}
