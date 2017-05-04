package com.mengyan.xin.manage.utils;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * <ul>
 * <li>对String类型的一些增强</li>
 * <li>1、方法一：请在此区域内填写方法描述</li>
 * <li>2、方法二：请在此区域内填写方法描述</li>
 * <li>User:weiwei Date: 2014年11月13日 time:下午11:16:45</li>
 * </ul>
 */
public class StringUtil {

    /**
     * 把数组组合成字符串，使用 splitChar 分割。
     *
     * @param value 需要组合的数组
     * @return 组合后的字符串
     * @roseuid 3C85E082021C
     */
    public static String assemble(String[] value, char splitChar) {
        if (value == null || value.length == 0) {
            return "";
        }

        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < value.length; i++) {
            temp.append(value[i]);
            temp.append(splitChar);
        }
        temp.deleteCharAt(temp.length() - 1);
        return temp.toString();
    }

    /**
     * 判断val是否在数组中存在
     * @param value
     * @param val
     * @return
     */
    public static boolean haveValue(String[] value,String val){
        if(value == null || value.length==0 || isNull(val)){
            return false;
        }
        for(String str:value){
            if(str.equals(val)){
                return true;
            }
        }
        return false;
    }

    /**
     * 把List组合成字符串，使用 splitChar 分割。
     *
     * @param value 需要组合的List
     * @return 组合后的字符串
     */
    public static String assemble(List<String> value, char splitChar) {
        if (value == null || value.size() == 0) {
            return "";
        }
        StringBuilder temp = new StringBuilder();
        for (String s : value) {
            temp.append(s);
            temp.append(splitChar);
        }
        temp.deleteCharAt(temp.length() - 1);
        return temp.toString();
    }

    /**
     * 对字符串 - 在左边填充指定符号
     *
     * @param s
     * @param fullLength
     * @param addSymbol
     * @return
     */
    public static String addSymbolAtLeft(String s, int fullLength,
                                         char addSymbol) {
        if (s == null) {
            return null;
        }
        int distance = 0;
        String result = s;
        int length = s.length();
        distance = fullLength - length;

        if (distance > 0) {
            char[] newChars = new char[fullLength];
            for (int i = 0; i < length; i++) {
                newChars[i + distance] = s.charAt(i);
            }
            for (int j = 0; j < distance; j++) {
                newChars[j] = addSymbol;
            }
            result = new String(newChars);
        }
        return result;
    }

    /**
     * 过滤空格
     *
     * @param o 要过滤的对象
     * @return 返回过滤后的结果
     */
    public static String filter(Object o) {
        if (o == null) {
            return "";
        }
        if ("null".equals(o)) {
            return "";
        }
        return o.toString().trim();
    }

    /**
     * 判断是否符合用户名格式标准
     *
     * @param str
     * @return
     */
    public static boolean isUserName(String str) {
        if (str == null || str == "") {
            return false;
        }
        String regex = "[^\\u4e00-\\u9fa5&&^\\w\\S]{6,29}$";
        return str.matches(regex);
    }

    /**
     * 判断是否是数字
     *
     * @param num    数值字符串
     * @param length 长度
     * @return
     */
    public static boolean isNumber(String num, int length) {
        if (num == null || num == "") {
            return false;
        }
        String regex = "[0-9]{0," + length + "}";

        return num.matches(regex);
    }

    /**
     * 判断是否为整数
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        return str.matches("^[0-9]\\d*$");
    }

    /**
     * 在流水号等字符串前加制表符，以避免该串在csv中显示为科学计数法
     *
     * @param o 要转换的对象
     * @return 返回转换后的结果
     */
    public static String toTextFormat(String o) {
        StringBuilder b = new StringBuilder("\t");
        return b.append(filter(o)).toString();
    }

    /**
     * @param str 传入科学计数法
     * @return 转换后的文本格式返回
     */
    public static String transCharByBigDecimal(String str) {
        BigDecimal bd;
        if (str == null || str == "") {
            return "";
        } else {
            bd = new BigDecimal(str);
        }
        return bd.toPlainString();
    }

    /**
     * 字符串分割成数组
     *
     * @param str
     * @param reg
     * @return
     */
    public static String[] splits(String str, String reg) {
        if (str == null || str == "") {
            return new String[]{};
        } else if (str.indexOf(reg) < 0) {
            return new String[]{str};
        }
        // 去除最后有个
        if (str.lastIndexOf(reg) > 0
                && (str.length() - 1 == str.lastIndexOf(reg))) {
            str = str.substring(0, str.lastIndexOf(reg));
        }
        return str.split(reg);
    }

    /**
     * 字符串分割成List
     *
     * @param str
     * @param reg
     * @return
     */
    public static List<String> split(String str, String reg) {
        String[] strs;
        if (str == null || str == "") {
            strs = new String[]{};
        } else if (str.indexOf(reg) < 0) {
            strs = new String[]{str};
        } else {
            if (str.lastIndexOf(reg) > 0
                    && (str.length() - 1 == str.lastIndexOf(reg))) {
                // 去除最后有个 ,
                str = str.substring(0, str.lastIndexOf(reg));
            }
            strs = str.split(reg);
        }
        return Arrays.asList(strs);
    }

    /**
     * 字符串分割
     *
     * @param str           字符串
     * @param separatorChar 分隔符
     * @return
     */
    public static String[] splitExtends(String str, String separatorChar) {
        if (str == null) {
            return null;
        } else if ("".equals(str)) {
            return new String[]{""};
        } else {
            return StringUtils.splitPreserveAllTokens(str, separatorChar);
        }
    }

    /**
     * Object类型转换成String类型
     *
     * @param obj
     * @return
     */
    public static String getString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * Object类型转换成String类型
     *
     * @param obj
     * @return
     */
    public static String getString(Object obj, Object obj1) {
        if (obj == null) {
            obj = obj1;
        }
        return obj == null ? "" : String.valueOf(obj);
    }

    /**
     * List转换String类型
     *
     * @param list
     * @return
     */
    public static String getString(List<String> list, String reg) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (String str : list) {
            buffer.append(reg).append(str);
        }
        return buffer.substring(1);
    }

    /**
     * 获取异常信息
     *
     * @param e
     * @return
     */
    public static String getExceptionInfo(Exception e) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("className: ");
        buffer.append(e.getStackTrace()[0].getClassName());
        buffer.append(" ; methodName: ");
        buffer.append(e.getStackTrace()[0].getMethodName());
        return buffer.toString();
    }


    /**
     * 验证是否为手机号码
     *
     * @param mobils
     * @return
     */
    public static boolean isMobile(String mobils) {
        String reg = "^(1[3|4|5|8][0-9]+\\d{8})$";
        if (mobils == null || "".equals(mobils) || mobils.length() != 11) {
            return false;
        }
        return mobils.matches(reg);
    }

    /**
     * 判断字符串是否为null或""
     *
     * @param str 源字符串
     * @return null或""返回true，否则返回false
     */
    public static boolean isNull(String str) {
        if (str == null) {
            return true;
        }
        return (str.trim()).length() == 0 || str.equals("");
    }

    /**
     * 获取6位数随机码
     *
     * @param len
     * @return
     */
    public static String genRandomString(int len) {
        final char[] mm = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        StringBuffer sb = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < len; i++) {
            sb.append(mm[random.nextInt(mm.length)]);
        }
        return sb.toString();
    }

    /**
     * 得到多少位随机数
     *
     * @param length 长度
     * @return
     */
    public static String getRandom(int length) {
        String res = "";
        StringBuffer buf = new StringBuffer();
        buf.append(res);
        for (int i = 0; i < length; i++) {
            buf.append(RandomStringUtils.random(10));
        }
        res = buf.toString();
        return res;
    }

    /**
     * 反正科学计数法
     *
     * @param d
     * @return
     */
    public static String doublieFormat(double d) {
        return new DecimalFormat("0").format(d);
    }

    /**
     * 字符串分割成List
     *
     * @param str 需要分割的字符串
     * @param reg 分隔符
     * @return 分割后List
     */
    public static List<String> splitList(String str, String reg) {
        String[] strs;
        if (StringUtils.isEmpty(str)) {
            strs = new String[]{};
        } else if (!str.contains(reg)) {
            strs = new String[]{str};
        } else {
            if (str.lastIndexOf(reg) > 0 && (str.length() - 1 == str.lastIndexOf(reg))) {
                // 去除最后有个 ","
                str = str.substring(0, str.lastIndexOf(reg));
            }
            strs = StringUtils.split(str, reg);
        }
        return Arrays.asList(strs);
    }

}
