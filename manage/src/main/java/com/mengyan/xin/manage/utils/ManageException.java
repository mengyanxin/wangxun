package com.mengyan.xin.manage.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Zhuojia on 16/4/14.
 */
@Getter
@Setter
@ToString
/**
 * Manage层异常封装
 */
public class ManageException extends Exception {
    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 无参构造函数
     */
    public ManageException() {
        super();
        this.errorCode = Constants.DATABASE_EXCEPTION_FLAG;
    }

    /**
     * 构造函数
     * @param errorCode 错误代码
     * @param message 错误信息
     */
    public ManageException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 构造函数
     * @param message 错误信息
     */
    public ManageException(String message) {
        super(message);
        this.errorCode = Constants.FAIL_FLAG;
    }

}
