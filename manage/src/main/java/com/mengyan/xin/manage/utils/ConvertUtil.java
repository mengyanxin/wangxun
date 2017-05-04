package com.mengyan.xin.manage.utils;

import com.google.common.net.InetAddresses;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 服务层数据转换工具
 */
public class ConvertUtil {

    /**
     * 无符号long型型IP地址转化为字符串
     * @param ip int型IP地址
     * @return 标准IP地址
     */
    public static String integerToStringIPv4(int ip) {
        return ((ip >> 24 ) & 0xFF) + "." +
                ((ip >> 16 ) & 0xFF) + "." +
                ((ip >>  8 ) & 0xFF) + "." +
                ( ip        & 0xFF);
    }

    /**
     * 字符串IP地址转化为无符号Long型
     * @param addr 标准IP地址
     * @return Long型IP地址
     */
    public static Long StringIPv4ToLong(String addr) {
        String[] addrArray = addr.split("\\.");

        long num = 0;

        for (int i=0;i<addrArray.length;i++) {
            int power = 3-i;
            num += ((Integer.parseInt(addrArray[i])%256 * Math.pow(256,power)));

        }
        return num;
    }

    /**
     * -255~255符号整型ip地址转化为字符串 Google工具
     * @param ip int型IP地址
     * @return 标准IP地址
     */
    public static String integerToStringIPv4Google(int ip) {
        return InetAddresses.fromInteger(ip).toString();
    }

    /**
     * ip地址转化为-255~255符号整型 Google工具
     * @param addr 标准IP地址
     * @return int型IP地址
     */
    public static Integer StringIPv4ToIntegerGoogle(String addr) {
        try {
            return InetAddresses.coerceToInteger(InetAddress.getByName(addr));
        } catch (UnknownHostException e) {
            return 0;
        }
    }
}
