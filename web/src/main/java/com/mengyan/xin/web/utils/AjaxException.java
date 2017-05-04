package com.mengyan.xin.web.utils;

import com.mengyan.xin.manage.utils.ManageException;

/**
 * 网络层异常
 */
public class AjaxException extends ManageException {

    //无参构造函数
    public AjaxException() {
        super();
    }
    //构造函数
    public AjaxException(String errorCode, String message) {
        super(errorCode, message);
    }
    //构造函数
    public AjaxException(String message) {
        super(message);
    }
}
