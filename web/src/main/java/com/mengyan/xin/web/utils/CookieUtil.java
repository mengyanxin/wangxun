package com.mengyan.xin.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 *  Cookie操作类 User:weiwei Date: 2014年12月27日 time:下午1:20:28
 */
public class CookieUtil {

	/**
	 * 设置一个Cookie
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static Cookie setCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(30 * 60);
		cookie.setPath("/");
		return cookie;
	}

	/**
	 * 设置一个Cookie
	 * 
	 * @param key
	 * @param value
	 * @param timeOut
	 * @return
	 */
	public static Cookie setCookie(String key, String value,
                                   Integer timeOut) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(timeOut);
		cookie.setPath("/");
		return cookie;
	}

	/**
	 * 根据一个Cookie可以获取Value
	 * 
	 * @param cookies
	 * @param key
	 * @return
	 */
	public static String getCookieValue(Cookie[] cookies, String key) {
		if (cookies == null) {
			return "";
		} else if (key == null && "".equals(key)) {
			return "";
		}

		for (Cookie cookie : cookies) {
			if (key.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return "";
	}

	/**
	 * 移除一个Cookie
	 * 
	 * @param cookies
	 * @param key
	 * @return
	 */
	public static boolean removeCookie(Cookie[] cookies, String key) {
		if (cookies == null) {
			return false;
		} else if (key == null && "".equals(key)) {
			return false;
		}

		for (Cookie cookie : cookies) {
			if (key.equals(cookie.getName())) {
				cookie.setMaxAge(0);
				return true;
			}
		}
		return false;
	}

	/**
	 * 移除所有Cookie
	 * 
	 * @param cookies
	 * @return
	 */
	public static boolean removeCookie(Cookie[] cookies, HttpServletResponse response) {
		if (cookies == null) {
			return true;
		}
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		return true;
	}
}
