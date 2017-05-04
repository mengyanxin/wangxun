package com.mengyan.xin.manage.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <ul>
 *     <li>简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:</li>
 *     <li>1、持有Mapper的单例.</li>
 *     <li>2、泛型返回值转换.</li>
 *     <li>3、批量转换Collection中的所有对象.</li>
 *     <li>4、区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.</li>
 *     <li>User:ShuZhen Date: 2014-09-07</li>
 * </ul>
 */
public class BeanMapperUtil {

    /*** 持有Dozer单例, 避免重复创建DozerMapper消耗资源.***/
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 基于Dozer转换对象的类型
     * @param obj 需要转换的对象
     * @param toObj 转换后的类型
     * @param <T> 返回对象类型泛型
     * @return 返回对象
     */
    public static <T> T objConvert(Object obj, Class<T> toObj) {
        if (null == obj) {
            return null;
        }
        return dozer.map(obj, toObj);
    }

    /**
     * 基于Dozer转换对象的类型
     *
     * @param obj       需要转换的对象
     * @param toObj     转换后的类型
     * @param configure Mapping的配置
     * @param <T>       返回对象类型泛型
     * @return 返回对象
     */
    public static <T> T objConvert(Object obj, Class<T> toObj, BeanMappingBuilder configure) {
        if (null == obj) {
            return null;
        }
        DozerBeanMapper dozer = new DozerBeanMapper();
        dozer.addMapping(configure);
        return dozer.map(obj, toObj);
    }

    /**
     * 基于Dozer转换对象List的类型
     * @param objList 需要转换的对象List
     * @param toObj 转换后的类型
     * @param <T> 返回对象类型泛型
     * @return 返回对象
     */
    public static <T> List<T> objConvert(List objList, Class<T> toObj) {
        if (null == objList) {
            return null;
        }
        List<T> resultList = new ArrayList<>();
        for (Object tempObj:objList) {
            resultList.add(dozer.map(tempObj,toObj));
        }
        return resultList;
    }

    /**
     * 基于Dozer转换对象List的类型
     *
     * @param objList   需要转换的对象List
     * @param toObj     转换后的类型
     * @param configure Mapping的配置
     * @param <T>       返回对象类型泛型
     * @return 返回对象
     */
    public static <T> List<T> objConvert(List objList, Class<T> toObj, BeanMappingBuilder configure) {
        if (null == objList) {
            return null;
        }
        DozerBeanMapper dozer = new DozerBeanMapper();
        dozer.addMapping(configure);
        List<T> resultList = new ArrayList<>();
        for (Object tempObj : objList) {
            resultList.add(dozer.map(tempObj, toObj));
        }
        return resultList;
    }

    /**
     * 基于Dozer转换Collection中对象的类型
     * @param sourceList 需要转换的集合
     * @param toObj 转换后对象类型
     * @param <T> 返回对象类型泛型
     * @return 返回对象
     */
    public static <T> List<T> mapList(Collection<?> sourceList, Class<T> toObj) {
		if(null == sourceList){
			return null;
		}
        List<T> destinationList = new ArrayList<>();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, toObj);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中
     * @param source 需要转换的对象
     * @param toObj 转换后对象类型
     */
    public static void copy(Object source, Object toObj) {
        if (null != source) {
            dozer.map(source, toObj);
        }
    }

    /**
     * 转化BO为查询DO的HashMap对象
     * @param object BO模型
     * @return HashMap对象
     */
    public static Map<String,Object> convertHashMap(Object object) throws Exception{
        Field[] fields = object.getClass().getDeclaredFields();
        Map<String,Object> map = new HashMap<>();
        for (Field field: fields) {
            field.setAccessible(true);
            if(field.get(object) != null) {
                map.put(field.getName(), field.get(object));
            }
        }
        return map;
    }

    /**
     * 用信息Object中不为null的字段对目标Object进行更新
     * @param infoObject 信息Object
     * @param targetObject 目标Object
     */
    public static void objUpdate(Object infoObject, Object targetObject){
        Field[] infoFields = infoObject.getClass().getDeclaredFields();
        Field[] targetFields = targetObject.getClass().getDeclaredFields();
        try {
            for (Field info:infoFields) {
                info.setAccessible(true);
                if (info.get(infoObject) != null && !info.getName().equals("serialVersionUID") ){
                    for (Field target:targetFields) {
                        target.setAccessible(true);
                        if (target.getName() == info.getName()){
                            target.set(targetObject,info.get(infoObject));
                            break;
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

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

    /**
     * 把bean放入一个map
     * @param object
     * @param map
     * @return
     * @throws Exception
     */
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
