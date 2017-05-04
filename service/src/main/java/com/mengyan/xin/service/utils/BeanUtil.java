package com.mengyan.xin.service.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/11/25.
 */
public class BeanUtil {

    /**
     * 把bean转换为map,包括null值
     * @param object
     * @return
     * @throws Exception
     */
    public static Map<String,Object> converMap(Object object) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        Class clazz = object.getClass();
        //获取所有字段
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            //设置私有属性可访问
            field.setAccessible(true);
            map.put(field.getName(),field.get(object));
        }
        return map;
    }

    public static Map<String,Object> parseMap(Object object,Map<String,Object> map) throws Exception{
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            map.put(field.getName(),field.get(object));
            if(field.getType() == Date.class && null != field.get(object)){
                map.put(field.getName(), DateFormatUtils.format((Date)field.get(object),"yyyy-MM-dd HH:mm:ss"));
            }
        }
        return map;
    }

}
