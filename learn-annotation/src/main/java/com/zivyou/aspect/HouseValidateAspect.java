package com.zivyou.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class HouseValidateAspect {

    // Spring AOP 不支持构造函数拦截，需要删除构造函数相关的 pointCut 和 advise

    @Pointcut("execution(* com.zivyou.house.MyHouse.open())")
    public void pointCut2() {

    }

    @Before("pointCut2()")
    public void advise2(JoinPoint joinPoint){
        log.info("========================================>");
    }
}
