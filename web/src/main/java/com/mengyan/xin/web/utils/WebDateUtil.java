package com.mengyan.xin.web.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 
 *
 * <ul>
 * <li>日期类型帮助类</li>
 * <li>User:weiwei Date: 2014年11月13日 time:下午2:00:55</li>
 * </ul>
 *
 */
public class WebDateUtil {
	
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
	 * 得到指定月的最后一天的日期
	 * @param month yyyyMM 如：201309 返回 20130930
	 * @return
	 */
	public static String getLastDayFromMonth(String month) {
		if (month.length() != 6) {
			return "";
		}
		DateTime dt = DateTime.parse(month,DateTimeFormat.forPattern(DATE_MONTH_FORMAT));
		
		return month + "" + dt.dayOfMonth().getMaximumValue();
	}

	/**
	 * 得到指定月的第一天的日期
	 * @param month yyyyMM 如：201309 返回 20130901
	 * @return
	 */
	public static String getFirstDayFromMonth(String month) {
		if (month.length() != 6) {
			return "";
		}
		DateTime dt = DateTime.parse(month,DateTimeFormat.forPattern(DATE_MONTH_FORMAT));
		
		return month + "0" + dt.dayOfMonth().getMinimumValue();
	}

	/**
	 * 得到今天指定格式的日期
	 * @param format 日期格式
	 * @return
	 */
	public static String getToDay(String format) {
		if (StringUtils.isEmpty(format)) {
			format = DATE_SHORT_FORMAT;
		}
		return new DateTime().toString(format);
	}

	/**
	 * 得到昨天的日期
	 * @return YYYYMMDD
	 */
	public static String getYesterdayDate(String format) {
		if (StringUtils.isEmpty(format)) {
			format = DATE_SHORT_FORMAT;
		}
		return WebDateUtil.getAroundDateByDay(-1, WebDateUtil.getToDay(format),format);
	}

	/**
	 * 得到指定日期的相隔日期
	 * @param cycleNum   相隔多少天
	 * @param inDate     传入的时间，格式：yyyyMMdd
	 * @param format     传出的时间格式
	 * @return
	 */
	public static String getAroundDateByDay(int cycleNum, String inDate,String format) {
		if (StringUtils.isEmpty(format)) {
			format = DATE_SHORT_FORMAT;
		}

		inDate = inDate.substring(0, 4) + "-" + inDate.substring(4, 6) + "-"+ inDate.substring(6, 8);
		DateTime dt = new DateTime(inDate);
		DateTime dtime = dt.plusDays(cycleNum);
		return dtime.toString(format);
	}

	/**
	 * 得到指定日期的相隔时间
	 * @param cycleNum   相隔多少分钟
	 * @param inDate     传入的时间，格式：yyyyMMddhhmmss
	 * @param format     传出的时间格式
	 * @return
	 */
	public static String getAroundDateByMinu(int cycleNum, String inDate,String format) {
		if (StringUtils.isEmpty(format)) {
			format = DATE_SHORT_FORMAT;
		}

		DateTime dt = new DateTime(Integer.valueOf(inDate.substring(0, 4)),
				Integer.valueOf(inDate.substring(4, 6)),
				Integer.valueOf(inDate.substring(6, 8)),
				Integer.valueOf(inDate.substring(8,10)),
				Integer.valueOf(inDate.substring(10, 12)),
				Integer.valueOf(inDate.substring(12, 14)), 0);
		DateTime time = dt.minusMinutes(cycleNum);
		return time.toString(format);
	}

	/**
	 * 得到指定日期的相隔日期
	 * @param cycleNum 相隔多少小时
	 * @param inDate   传入的时间，格式：yyyyMMdd
	 * @param format   传出的时间格式
	 * @return
	 */
	public static String getAroundDateByHour(int cycleNum, String inDate,String format) {
		if (StringUtils.isEmpty(format)) {
			format = DATE_SHORT_FORMAT;
		}

		DateTime dt = new DateTime(Integer.valueOf(inDate.substring(0, 4)),
				Integer.valueOf(inDate.substring(4, 6)), 
				Integer.valueOf(inDate.substring(6, 8)), 
				Integer.valueOf(inDate.substring(8,10)), 
				Integer.valueOf(inDate.substring(10, 12)),
				Integer.valueOf(inDate.substring(12, 14)), 0);
		DateTime dtime = dt.minusHours(cycleNum);
		return dtime.toString(format);
	}

	/**
	 * 得到指定日期的相隔年数的日期
	 * @param cycleNum 相隔多少年
	 * @param inDate   传入的时间，格式：yyyyMMdd
	 * @param format   传出的时间格式
	 * @return String
	 */
	public static String getAroundDateByYear(int cycleNum, String inDate,
			String format) {
		if (StringUtils.isEmpty(format)) {
			format = DATE_SHORT_FORMAT;
		}

		DateTime dt = new DateTime(Integer.valueOf(inDate.substring(0, 4)),
				Integer.valueOf(inDate.substring(4, 6)), 
				Integer.valueOf(inDate.substring(6, 8)), 0, 0, 0, 0);
		
		return dt.plusYears(cycleNum).toString(format);
	}


	/**
	 * 得到指定日期的相隔月数的日期
	 * @param cycleNum   相隔多少年
	 * @param inDate     传入的时间，格式：yyyyMMdd
	 * @param format     传出的时间格式
	 * @return
	 */
	public static String getAroundDateByMonth(int cycleNum, String inDate,
			String format) {
		if (StringUtils.isEmpty(format)) {
			format = DATE_SHORT_FORMAT;
		}

		DateTime dt = new DateTime(Integer.valueOf(inDate.substring(0, 4)),
				Integer.valueOf(inDate.substring(4, 6)), 
				Integer.valueOf(inDate.substring(6, 8)), 
				Integer.valueOf(inDate.substring(8,10)), 
				Integer.valueOf(inDate.substring(10, 12)),
				Integer.valueOf(inDate.substring(12, 14)));
		DateTime dtime = dt.plusMonths(cycleNum);

		return dtime.toString(format);
	}

	/**
	 * 计算两个日期的间隔天数
	 * @param startDate 开始时间，如：2008-12-03 11:00:00
	 * @param endDate   结束时间，如：2009-12-31 11:00:00
	 * @return long     间隔天数(long)
	 */
	public static long getBetweenDays(Date startDate, Date endDate) {
		if (endDate == null || startDate == null) {
			return -1;
		}
		Long days = endDate.getTime() - startDate.getTime();

		return days / (1000 * 60 * 60 * 24);
	}

	/**
	 * 验证输入的文本信息日期是否合
	 * @param dateStr
	 * @return
	 */
	public static Date isDate(String dateStr) {
		String date_format_1 = "yyyy/MM/dd";
		String date_format_2 = "yyyy-MM-dd";
		String date_format_3 = "yyyyMMdd";
		String date_format_4 = "yyyy.MM.dd";
		String[] date_format = { date_format_1, date_format_2, date_format_3,date_format_4 };
		for (int i = 0; i < date_format.length; i++) {
			Date tempDate = isDate(dateStr, date_format[i]);
			if (null != tempDate) {
				return tempDate;
			}
		}
		return null;
	}

	/**
	 * 验证输入的文本信息日期是否合
	 * @param dateStr
     * @param patternString
	 * @return Date
	 */
	public static Date isDate(String dateStr, String patternString) {
		if (StringUtils.isEmpty(patternString)) {
			patternString = WebDateUtil.DATE_SHORT_FORMAT;
		}
		try {
			FastDateFormat formatDate = FastDateFormat.getInstance(patternString);
			ParsePosition pos = new ParsePosition(0);
			Date tempDate = (Date) formatDate.parseObject(dateStr, pos);
			tempDate.getTime();
			return tempDate;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * date 转String
	 * 
	 * @param date 日期
	 * @param pattern 日期格式
	 * @return
	 */
	public static String pareDate(Date date, String pattern) {
		if (null == date) {
			return null;
		}
		if (StringUtils.isEmpty(pattern)) {
			pattern = DATE_SHORT_FORMAT;
		}
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * date 转String
	 *
	 * @param date 日期
	 * @return
	 */
	public static String pareDate(Date date) {
		if (null == date) {
			return null;
		}
		return DateFormatUtils.format(date, DATE_TIME_FORMAT);
	}

	/**
	 * String 转date
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date pareDate(String dateStr, String pattern) {
		if (null == dateStr) {
			return null;
		}
		if (StringUtils.isEmpty(pattern)) {
			pattern = DATE_SHORT_FORMAT;
		}
		DateTime dt = DateTime.parse(dateStr,DateTimeFormat.forPattern(pattern));
		return dt.toDate();
	}

	/**
	 * String 转Date类型 格式('EEE MMM dd HH:mm:ss zzz yyyy')
	 * @param str
	 * @return
	 */
	public static Date parseDate(String str) {
		if(str == null) {
			return null;
		}
		try {
			return new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * String 转Date类型 格式('yyyy-MM-dd')
	 * @param str
	 * @return
	 */
	public static Date parseDate2(String str) {
		if(str == null) {
			return null;
		}
		try {
			return new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 当前月往前推
	 * 
	 * @param date   日期
	 * @return Date  日期
	 */
	public static Date getLastMonth(Date date, int cycle) {
		DateTime datetime = new DateTime(date);
		return datetime.minusMonths(cycle).toDate();
	}

	/**
	 * 得到两个时间相隔的秒
	 * @param beginDate 开始日期
	 * @param endDate   结束日期
	 * @return Long
	 */
	public static Long getAroundMinute(String beginDate,String endDate){
		long i;
		i = Seconds.secondsBetween(getDateTime(beginDate, DATE_TIMES_FORMAT),
				getDateTime(endDate, DATE_TIMES_FORMAT)).getSeconds();
		return i;
	}

	/**
	 * 通过指定日期和格式，获取日期时间
	 * @param inDate 指定日期
	 * @param format 日期格式
	 * @return DateTime
	 */
	public static DateTime getDateTime(String inDate,String format){
		if(StringUtils.isEmpty(format)){
			format= DATE_SHORT_FORMAT;
		}
		return DateTime.parse(inDate,DateTimeFormat.forPattern(format));
	}


}
