package com.mengyan.xin.manage.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * AspectJ切面小工具
 */
@Slf4j
public class AspectUtil {
    /**
     * 获取连接点的方法
     * @param joinPoint 连接点
     * @return 连接点触发的方法
     */
    static public Method getInvokeMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        return methodSignature.getMethod();
    }

    /**
     * 获取连接点方法的名称
     * @param joinPoint 连接点
     * @return 连接点方法的方法名
     */
    static public String getInvokeMethodName(JoinPoint joinPoint) {
        String methodName = joinPoint.getTarget().getClass().getName() + ".";
        methodName += getInvokeMethod(joinPoint).getName();
        return methodName;
    }

    /**
     * 获得包含关键字的Field
     * @param joinPoint 连接点
     * @param keyword 关键字
     * @return Field数组
     */
    static public List<Field> getFieldsContainsKey(JoinPoint joinPoint, String keyword) {
        List<Field> fieldList = new ArrayList<>();
        Field[] fields = joinPoint.getTarget().getClass().getDeclaredFields();
        for (Field field:fields) {
            if(field.getName().contains(keyword)) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    /**
     * 获得包含关键字的第一个Field
     * @param joinPoint 连接点
     * @param keyword 关键字
     * @return Field
     */
    static public Field getFirstFieldContainsKey(JoinPoint joinPoint, String keyword) {
        List<Field> list = getFieldsContainsKey(joinPoint,keyword);
        if (!list.isEmpty()) {
            return list.get(0);
        }else {
            log.debug("无法获取到包含关键字的Field");
            return null;
        }
    }

    /**
     * 获得类名中包含关键字的参数
     * @param joinPoint 连接点
     * @param keyword 关键字
     * @return 参数数组
     */
    static public List<Object> getParamtersContainsKey(JoinPoint joinPoint, String keyword) {
        List<Object> objectList = new ArrayList<>();
        for (Object temp:joinPoint.getArgs()) {
            if (temp.getClass().getName().contains(keyword)) {
                objectList.add(temp);
            }
        }
        return objectList;
    }

    /**
     * 获得类名中包含关键字的第一个参数
     * @param joinPoint 连接点
     * @param keyword 关键字
     * @return 参数
     */
    static public Object getFirstParameterContainsKey(JoinPoint joinPoint, String keyword) {
        List<Object> list = getParamtersContainsKey(joinPoint,keyword);
        if (list.isEmpty()) {
            log.debug("无法获取到包含关键字{}的参数",keyword);
            return null;
        }
        return list.get(0);
    }

}


