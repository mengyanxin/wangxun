package com.mengyan.xin.service.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则规则的比较判断是否一致
 */
public class RegularUtil {
    /**
     * 正则规则判断
     * @param rule    正则式
     * @param source   比较源
     * @return
     * @throws Exception   异常
     */
    public static boolean judgeRule(String rule,String source) throws Exception {
        Pattern pattern=Pattern.compile(rule);//编译成模式
        Matcher matcher=pattern.matcher(source);//创建一个匹配器
        boolean result = matcher.find();
        return result;
    }
}
