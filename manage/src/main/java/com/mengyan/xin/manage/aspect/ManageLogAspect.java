package com.mengyan.xin.manage.aspect;

import com.mengyan.xin.manage.utils.AspectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by Zhuojia on 2016/9/26.
 */
@Aspect
@Slf4j
@Component
public class ManageLogAspect {

    @Pointcut(value = "execution(public * com.mengyan.xin.manage.impl..*.*(..))")
    public void logManage() {

    }

    @AfterThrowing(value = "logManage()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        try {
            String methodName = AspectUtil.getInvokeMethodName(joinPoint);
            String message = (ex.getMessage() == null)?ex.getClass().getName():ex.getMessage();
            Object[] args = joinPoint.getArgs();
            log.warn("{}方法抛出异常: {} 方法参数：{}",methodName,message,args);
        } catch (Exception e) {
            log.error("Manage切面方法异常: {}",e.getMessage());
        }
    }

    @Before(value = "logManage()")
    public void logBefore(JoinPoint joinPoint) {
        try {
            String methodName = AspectUtil.getInvokeMethodName(joinPoint);
            Object bo = AspectUtil.getFirstParameterContainsKey(joinPoint,"BO");
            log.info("{}方法调用开始 BO参数：{}",methodName,bo);
        } catch (Exception e) {
            log.error("Manage切面方法异常: {}",e.getMessage());
        }
    }

    @AfterReturning(value = "logManage()", returning = "r")
    public void logAfterReturning(JoinPoint joinPoint, Object r) {
        try {
            String methodName = AspectUtil.getInvokeMethodName(joinPoint);
            log.info("{}方法调用结束 Return值：{}",methodName,r.toString());
        } catch (Exception e) {
            log.error("Manage切面方法异常: {}",e.getMessage());
        }
    }
}
