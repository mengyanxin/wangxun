package com.mengyan.xin.manage.utils;

/**
 * Created by 刘奇 on 16/4/5.
 */
public class Constants {

    /************************************** 常量 ******************************************/
    /**
     * 每页显示信息条数
     */
    public final static int PAGE_SIZE = 10;
    public final static int ONE_WORM_ADD = 1;
    /**
     * Web层常量
     */

    public final static String MSG = "message";
    public final static String ERROR = "code";
    public final static String RESPONSE = "data";

    /**
     * SESSION字段
     */
    public final static String SESSION_USER_ACCOUNT = "SESSION_USER_ACCOUNT"; //SESSION用户名

    public final static String SESSION_USER_ID = "SESSION_USER_ID"; //SESSION用户ID

    public final static String SESSION_USER_STATUS = "SESSION_USER_STATUS"; //SESSION用户状态

    public final static String SESSION_USER_ROLE = "SESSION_USER_ROLE"; //SESSION用户角色

    public final static String SESSION_USER_ENCRYPTED_VALIDATION = "SESSION_USER_ENCRYPTED_VALIDATION"; //SESSION用户密保验证

    /**
     * Log日志常量
     */

    public final static String LOG_TRACE_ID = "TRACE_ID";

    /************************************** 异常 ******************************************/

    /**
     * 通用异常
     */
    public final static String SUCCESS_FLAG = "000000"; //通用成功

    public final static String FAIL_FLAG = "999999"; //异常报错专用

    public final static String FAIL_EXCEPTION_FLAG = "900001"; //异常报错专用

    public final static String FAIL_EXCEPTION_MSG = "通用抛出异常[上线后更改]";

    public final static String PARAMETER_ERROR_FLAG = "300101";

    public final static String PARAMETER_ERROR_MSG = "请求参数异常";

    public final static String SESSION_LOGIN_FLAG = "900099";   //用户未登录专用

    public final static String SESSION_LOGIN_MSG = "连接超时或未登录，请重新登录";

    /**
     * Dal层异常
     */

    public final static String DATABASE_EXCEPTION_FLAG = "100101";

    public final static String DATABASE_EXCEPTION_MSG = "数据库读写错误";

    /**
     * Manage层异常
     */
    public final static String MANAGE_PARAMETER_ERROR_FLAG = "200101";

    public final static String MANAGE_PARAMETER_ERROR_MSG = "数据库查询参数错误";

    public final static String MANAGE_REFLECTION_EXCEPTION_FLAG = "200999";

    public final static String MANAGE_REFLECTION_EXCEPTION_MSG = "Manage层调用DAL层反射函数错误";

    public final static String MANAGE_METHOD_FAIL_FLAG = "200998";

    public final static String MANAGE_METHOD_FAIL_MSG = "Manage层调用DAL层返回值错误";

    public final static String MANAGE_INSERT_FAIL_FLAG = "200901";

    public final static String MANAGE_INSERT_FAIL_MSG = "保存数据失败";

    public final static String MANAGE_INSERT_MSG_DUPLICATE = "输入内容已存在";

    public final static String MANAGE_DELETE_FAIL_FLAG = "200902";

    public final static String MANAGE_DELETE_FAIL_MSG = "删除数据失败";

    public final static String MANAGE_UPDATE_FAIL_FLAG = "200903";

    public final static String MANAGE_UPDATE_FAIL_MSG = "更新数据失败";

    public final static String MANAGE_SELECTED_FAIL_FLAG = "200904";

    public final static String MANAGE_SELECTED_FAIL_MSG = "查询数据不存在";

    /**
     * Service层异常
     */

    public final static String SERVICE_REFLECTION_EXCEPTION_FLAG = "300999";

    public final static String SERVICE_REFLECTION_EXCEPTION_MSG = "Service层调用Manage层反射函数错误";

    public final static String SERVICE_METHOD_FAIL_FLAG = "300998";

    public final static String SERVICE_METHOD_FAIL_MSG = "Service层调用Manage层返回值错误";

    public final static String SERVICE_VALIDATION_FAIL_FLAG = "300901";

    public final static String SERVICE_VALIDATION_FAIL_MSG = "保存数据失败";

    public final static String SERVICE_INSERT_FAIL_FLAG = "300901";

    public final static String SERVICE_INSERT_FAIL_MSG = "保存数据失败";

    public final static String SERVICE_DELETE_FAIL_FLAG = "300902";

    public final static String SERVICE_DELETE_FAIL_MSG = "删除数据失败";

    public final static String SERVICE_UPDATE_FAIL_FLAG = "300903";

    public final static String SERVICE_UPDATE_FAIL_MSG = "更新数据失败";

    public final static String SERVICE_SELECTED_FAIL_FLAG = "300904";

    public final static String SERVICE_SELECTED_FAIL_MSG = "查询数据条件有误或不存在";

    /**
     * Web层异常
     */

    public final static String WEB_UPLOAD_FAIL_FLAG = "400201";

    public final static String WEB_UPLOAD_FAIL_MSG = "文件上传失败";

}
