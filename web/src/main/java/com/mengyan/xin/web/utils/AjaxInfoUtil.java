package com.mengyan.xin.web.utils;

import com.mengyan.xin.manage.utils.Constants;
import com.mengyan.xin.manage.utils.ExceptionUtil;
import com.mengyan.xin.manage.utils.ManageException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户Ajax返回数据使用
 * <li>1.response数据到页面，用于ajax请求
 * <li>2.response数据到页面，用于ajax请求
 * User:weiwei Date: 2014年11月13日 time:下午7:05:13
 */
@Slf4j
public class AjaxInfoUtil {

    /**
     * response数据到页面，用于ajax请求
     * @param messages    请求参数
     * @param response    响应
     */
	public static void setJSONResponse(Map<String, Object> messages,
									   HttpServletResponse response) {

        Writer writer = null;
		PrintWriter out = null;
		try {
			response.setContentType("application/json;charset=UTF-8");
			out = response.getWriter();
			out.print(JSONObject.fromObject(messages).toString());
			log.info("Response信息:"+JSONObject.fromObject(messages).toString());
			out.flush();
		} catch (IOException e) {
			log.error("setJSONResponse(Map<String, Object> messages):", e);
		} finally {
			responseCheck(writer,out);
		}
	}

    /**
     * response数据到页面，用于ajax请求
     * @param plainText   请求参数
     * @param response  响应
     */
	public static void setTextResponse(String plainText, HttpServletResponse response) {

        Writer writer = null;
		PrintWriter out = null;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.print(plainText);
			out.flush();
		} catch (IOException e) {
			log.error("{}", e);
		} finally {
			responseCheck(writer,out);
		}

	}

	/**
	 * 完毕后做静态检查
     */
	public static void responseCheck(Writer writer, PrintWriter out) {
		try {
			if (out != null) {
				out.close();
			}
			if (writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			log.error("关闭流失败：{}", e);
		}
	}

	/**
	 * 返回未登录JSON
	 * @param errorCode 错误编号
	 * @param errorMsg 错误信息
	 */
	public static void setFailureResponse(String errorCode, String errorMsg, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>(); //结果集合
		responseMap.put(Constants.ERROR,errorCode);
		responseMap.put(Constants.MSG,errorMsg);
		setJSONResponse(responseMap,response);
	}

	/**
	 * 返回通用错误JSON
	 * @param errorMsg 错误信息
     */
	public static void setFailureResponse(String errorMsg, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>(); //结果集合
		responseMap.put(Constants.ERROR,Constants.FAIL_FLAG);
		responseMap.put(Constants.MSG,errorMsg);
		setJSONResponse(responseMap,response);
	}

	/**
	 * 返回通用错误JSON
	 * @param exception 错误信息
	 */
	public static void setFailureResponse(Exception exception, HttpServletResponse response) {

		Map<String, Object> responseMap = new HashMap<String, Object>();//结果集合

		Throwable target = ExceptionUtil.getInvocationTarget(exception);

        if (target instanceof ManageException) {
            responseMap.put(Constants.ERROR, ((ManageException) target).getErrorCode());
            responseMap.put(Constants.MSG, target.getMessage());
		}else {
			responseMap.put(Constants.ERROR, Constants.FAIL_EXCEPTION_FLAG);
			responseMap.put(Constants.MSG, Constants.FAIL_EXCEPTION_MSG);
		}
		setJSONResponse(responseMap,response);
	}

	/**
	 * 返回通用成功JSON
	 * @param object 返回对象
     */
	public static void setSuccessResponse(Object object, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>(); //结果集合
		responseMap.put(Constants.ERROR,Constants.SUCCESS_FLAG);
		responseMap.put(Constants.MSG,"请求成功");
		responseMap.put(Constants.RESPONSE,object);
		setJSONResponse(responseMap,response);
	}

}
