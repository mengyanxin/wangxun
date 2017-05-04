package com.mengyan.xin.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * Created by Zhuojia on 16/9/8.
 */
public class ParameterInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String,String[]> parameterMap = request.getParameterMap();
        Set<String> keys = parameterMap.keySet();
        //无参数直接放过
        if (keys.isEmpty()) {
            return true;
        }
        //有参数
        Integer count = keys.size();
        for (String key:keys) {
            for (String value:parameterMap.get(key)){
                if (value.trim().isEmpty()) {
                    count--;
                }
            }
        }
        //全部为空参数拦截
        return count > 0;
    }
}
