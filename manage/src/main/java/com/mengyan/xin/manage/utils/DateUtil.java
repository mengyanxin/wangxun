package com.mengyan.xin.manage.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Zhuojia on 16/8/26.
 */
public class DateUtil {

    /**
     * 默认日期格式
     */
    public static final String DATE_SHOW_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SHORT_FORMAT = "yyyyMMdd";
    public static final String DATE_MONTH_FORMAT = "yyyyMM";
    public static final String DATE_TIMEC_FORMAT = "yyyyMMddHH24ssmm";
    public static final String DATE_TIMES_FORMAT = "yyyyMMddhhmmss";
    public static final String DATE_TIME_FORMAT_NO_SPLIT = "yyyyMMddHHmmss";

    /**
     * 格式化Date
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (null == date) {
            return null;
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATE_SHORT_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 格式化String
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date formatString(String date, String pattern) throws Exception{
        if (null == date || date.equals("") || date.length()<=0) {
            return null;
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATE_SHORT_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(date);
    }

    /**
     * 偏移日期
     * @param date 起始日期
     * @param offsetKey 偏移Calendar的Key
     * @param offsetNum 偏移数目
     */
    public static Date offsetDateWithZeroHMSS(Date date, int offsetKey, int offsetNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(offsetKey,calendar.get(offsetKey)+offsetNum);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    /**
     * 重置某个时间的Hour Minute Second MilliSecond为零
     * @param date
     * @return
     */
    public static Date zeroDateHMSS(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    /**
     * 重置某个时间的 Second MilliSecond为零
     * @param date
     * @return
     */
    public static Date zeroDateSS(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    /**
     * 重置传入时间的毫秒数
     * @param date 日期
     * @return
     */
    public static Date zeroDateSSS(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    /**
     * 获取日期的年份,返回int类型
     * @param date
     * @return
     */
    public static int getDateYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

}
