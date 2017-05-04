package com.mengyan.xin.manage.utils;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * 日志工具类
 */
public class LogUtil {
    /**
     * 对该处的日志打印上Log Id
     */
    public static void putTraceId() {
        MDC.put(Constants.LOG_TRACE_ID, UUID.randomUUID().toString());
    }
}
