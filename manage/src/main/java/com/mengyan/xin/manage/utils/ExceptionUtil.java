package com.mengyan.xin.manage.utils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Zhuojia on 2016/9/27.
 */
public class ExceptionUtil {

    static public Throwable getInvocationTarget(Exception e) {
        Throwable target = e;
        while (target instanceof InvocationTargetException) {
            target = ((InvocationTargetException) target).getTargetException();
        }
        return target;
    }
}

