package com.mengyan.xin.manage.utils;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhuojia on 16/4/25.
 */
public class ReflectionUtil {

    /**
     * 通过方法名和参数列表找方法
     * @param searchClass 从哪个类中找那发
     * @param name 方法名
     * @param args 方法要传入的参数Object列表,并不是Class列表
     * @return
     * @throws NoSuchMethodException
     */
    public static Method methodByNameAndArgsInClass(Class<?> searchClass, String name, Object... args) throws NoSuchMethodException {
        String methodName = name;
        Class[] argsClazz = new Class[args.length];
        for (int i = 0; i < args.length; i ++) {
            argsClazz[i] = args[i].getClass();
        }
        return searchClass.getDeclaredMethod(methodName, argsClazz);
    }
    /**
     * 通过名称找方法
     * @param searchClass 实例的类
     * @param name 方法名
     * @return 返回第一个找到该方法
     * @throws NoSuchMethodException
     */
    public static Method[] methodsByNameInClass(Class<?> searchClass, String name) throws NoSuchMethodException {
        Method[] methods = searchClass.getDeclaredMethods();
        List<Method> methodList = new ArrayList<>();
        for (Method method:methods) {
            if (name.equals(method.getName())) {
                methodList.add(method);
            }
        }
        if (methodList.size() != 0) {
            Method[] resultMethods = new Method[methodList.size()];
            return methodList.toArray(resultMethods);
        }
        throw new NoSuchMethodException();
    }

    /**
     * 通过名称找变量
     * @param clazzOrObject 从哪个Object里找(并非Class)
     * @param name 变量名
     * @return
     * @throws Exception
     */
    public static Field fieldByName(Object clazzOrObject, String name) throws Exception {
        Class clazz = clazzOrObject instanceof Class ? (Class) clazzOrObject : clazzOrObject.getClass();
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    /**
     * 获取 代理目标对象
     * @param proxy 代理对象
     * @return 目标对象
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {
        // 不是代理对象
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        // 如果是JdkDynamicProxy代理对象
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        // 如果是Cglib代理对象
        } else {
            return getCglibProxyTargetObject(proxy);
        }
    }

    /**
     * 获得 动态代理目标Class
     *
     * @param proxy 代理对象
     * @return 目标对象Class
     */
    public static Class getTargetClazz(Object proxy) {
        if (AopUtils.isAopProxy(proxy)) {
            return AopUtils.getTargetClass(proxy);
        } else {
            return proxy.getClass();
        }
    }

    /**
     * 获取Cglib目标对象
     * @param proxy 代理对象
     * @return 目标对象
     * @throws Exception
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return target;
    }




    /**
     * 获取JdkDynamicProxy目标对象
     * @param proxy  代理对象
     * @return 目标对象
     * @throws Exception
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }
}
