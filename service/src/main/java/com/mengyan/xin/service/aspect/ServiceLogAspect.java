package com.mengyan.xin.service.aspect;

import com.mengyan.xin.manage.utils.AspectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by Zhuojia on 2016/9/22.
 */
@Aspect
@Component
@Slf4j
public class ServiceLogAspect {

    @Pointcut(value = "execution(public * com.mengyan.xin.service.impl..*.*(..))")
    public void logService() {
    }

    @AfterThrowing(value = "logService()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        try {
            String methodName = AspectUtil.getInvokeMethodName(joinPoint);
            String message = (ex.getMessage() == null)?ex.getClass().getName():ex.getMessage();
            Object[] args = joinPoint.getArgs();
            log.warn("{}方法抛出异常: {} 方法参数：{}",methodName,message,args);
        } catch (Exception e) {
            log.error("Service切面方法异常: {}",e.getMessage());
        }
    }

    @Before(value = "logService()")
    public void logBefore(JoinPoint joinPoint) {
        try {
            String methodName = AspectUtil.getInvokeMethodName(joinPoint);
            Object reqDTO = AspectUtil.getFirstParameterContainsKey(joinPoint,"ReqDTO");
            log.info("{}方法调用开始 RequestDTO参数：{}",methodName,reqDTO);
        } catch (Exception e) {
            log.error("Service切面方法异常: {}",e.getMessage());
        }
    }

    @AfterReturning(value = "logService()", returning = "r")
    public void logAfterReturning(JoinPoint joinPoint, Object r) {
        try {
            String methodName = AspectUtil.getInvokeMethodName(joinPoint);
            log.info("{}方法调用结束 ResponseDTO参数：{}",methodName,r.toString());
        } catch (Exception e) {
            log.error("Service切面方法异常: {}",e.getMessage());
        }
    }

}
