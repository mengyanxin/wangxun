package com.mengyan.xin.web.aspect;

import com.mengyan.xin.manage.utils.AspectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Zhuojia on 16/9/21.
 */
@Aspect
@Slf4j
@Component
public class ControllerLogAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void logRequest() {

    }

    @Before("logRequest()")
    public void logBeforeRequest(JoinPoint joinPoint) {
        log.info("网络请求开始 {}",requestContent(joinPoint));
    }

    @After("logRequest()")
    public void logAfterRequest(JoinPoint joinPoint) {
        log.info("网络请求结束 {}",requestContent(joinPoint));
    }

    /**
     * 打印网络请求日志
     * @param joinPoint 切入处
     */
    private String requestContent(JoinPoint joinPoint) {
        StringBuilder url = new StringBuilder("RequestURL: ");
        //获取RootPath
        try {
            String[] rootUrls = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class).value();
            if (rootUrls.length > 0 ) {
                url.append(rootUrls[0]);
            }
        } catch (Exception e) {
            log.error("ControllerLogAspect切面获取RootRequestMap异常 JointPoint:{}",joinPoint.getTarget());
        }
        //获取MethodPath
        try {
            String[] subUrls = AspectUtil.getInvokeMethod(joinPoint).getAnnotation(RequestMapping.class).value();
            if (subUrls.length > 0 ) {
                url.append(subUrls[0]);
            }
        } catch (Exception e) {
            log.error("ControllerLogAspect切面获取MethodRequestMap异常 JointPoint:{}",joinPoint.getTarget());
        }
        //获取参数
        try {
            Object vo = AspectUtil.getFirstParameterContainsKey(joinPoint,"ReqVO");
            url.append(" RequestVO参数: ");
            url.append(vo);
        } catch (Exception e) {
            log.error("ControllerLogAspect切面获取RequestVO参数异常 JointPoint:{}",joinPoint.getTarget());
        }
        return url.toString();
    }
}
