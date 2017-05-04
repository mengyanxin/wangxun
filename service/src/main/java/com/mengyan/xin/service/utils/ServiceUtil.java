package com.mengyan.xin.service.utils;


import com.mengyan.xin.manage.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务层工具——通过反射调用Manage层通用函数
 */
@Slf4j
public class ServiceUtil {

    private final static String INSERT_METHOD = "insert";
    private final static String DELETE_METHOD = "delete";
    private final static String UPDATE_METHOD = "update";
    private final static String SELECT_METHOD = "select";
    private final static String SELECT_ALL_METHOD = "selectAll";


    private static Object invoke(Object reqObject, Object manage,boolean checkReqObj, String... methodNameAndArgs) throws Exception{
        //组合函数名
        String methodName = methodNameAndArgs[0] + splitTableName(manage);
        try {
            //模型检查
            if (checkReqObj && reqObject != null) {
                VerifyUtil.validateObject(reqObject);
            }
            //准备参数列表
            Object[] args;
            //根据是否传入reqObject拼接函数
            if (reqObject != null) {
                Class objectBOClazz = getBusinessObject(reqObject.getClass());
                Object objectBO = BeanMapperUtil.objConvert(reqObject,objectBOClazz);
                //组合参数
                args = new Object[methodNameAndArgs.length];
                args[0] = objectBO;
                for (int i = 1; i < args.length; i++) {
                    args[i] = methodNameAndArgs[i];
                }
            } else {
                //组合参数
                args = new Object[methodNameAndArgs.length-1];
                for (int i = 0; i < args.length; i++) {
                    args[i] = methodNameAndArgs[i+1];
                }
            }
            //触发函数
            Method method = ReflectionUtil.methodByNameAndArgsInClass(manage.getClass(),methodName,args);
            method.setAccessible(true);
            //返回Mananger层返回值
            Object resObj = method.invoke(manage, args);
            return resObj;
        } catch (ManageException e) {
            log.error("ServiceUtil校验参数{}有误: {}",reqObject.getClass().getName(),e.getMessage());
            throw e;
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (NoSuchMethodException e) {
            log.error("ServiceUtil无法找到反射方法{}: {}",methodName,e.getClass().getName());
            throw e;
        } catch (IllegalAccessException e) {
            log.error("ServiceUtil反射方法{} Access异常: {}",methodName,e.getClass().getName());
            throw e;
        } catch (InvocationTargetException e) {
            String exceptionName = e.getClass().getName();
            String targetName = ExceptionUtil.getInvocationTarget(e).getClass().getName();
            log.error("ServiceUtil反射方法{} Invoke异常: {} Target异常: {}",methodName,exceptionName,targetName);
            throw e;
        }
    }

    /**
     * 通过Manage拿出某张表格中Object的Key-Value对应
     *
     * @param manage
     * @param key     例如xxxId
     * @param value   例如 xxxName
     * @return
     * @throws Exception
     */
    public static Map<Object, Object> getAttributeMap(Object manage, String key, String value) throws Exception {
        //或得属性BO Class
        Class boClazz = getBusinessObject(manage);
        //获得Field
        Field keyFiled = ReflectionUtil.fieldByName(boClazz, key);
        Field valueField = ReflectionUtil.fieldByName(boClazz, value);
        //选出所有属性存入Map
        Object all = invoke(null, manage, false, SELECT_ALL_METHOD);
        Map<Object, Object> map = new HashedMap();
        for (Object temp : (List) all) {
            map.put(keyFiled.get(temp), valueField.get(temp));
        }
        return map;
    }
    /**
     * Service层调用Manage层工具——增
     * @param reqObject Request Data Transaction Object
     * @param manage Manage Interface Bean
     * @param resClass Response Data Transaction Object的Class类
     * @return 返回Service层的ResDTO
     */
    public static <T> T insert(Object reqObject, Object manage, Class<T> resClass, boolean checkReqObj) throws Exception {
        try {
            log.info("ServiceUtil调用INSERT方法 RequestDTO参数：{}",reqObject);
            //反射函数
            Object object = invoke(reqObject,manage,checkReqObj,INSERT_METHOD);
            if (object instanceof Integer) {
                if (((Integer)object).intValue() !=0) {
                    return selectSingle(reqObject,manage,resClass);
                }
            }
            throw new ManageException(Constants.SERVICE_INSERT_FAIL_FLAG, Constants.SERVICE_INSERT_FAIL_MSG);
        } catch (Exception e) {
            log.error("ServiceUtil调用INSERT方法异常：{}",e.getClass().getName());
            throw e;
        }
    }

    /**
     * Service层调用Manage层工具——删
     * @param reqObject Request Data Transaction Object
     * @param manage Manage Interface Bean
     * @param resClass Response Data Transaction Object的Class类
     * @return 返回Service层的ResDTO
     */
    public static <T> T delete(Object reqObject, Object manage, Class<T> resClass, boolean checkReqObj) throws Exception {
        try {
            log.info("ServiceUtil调用DELETE方法 RequestDTO参数：{}",reqObject);
            //反射函数
            Object object = invoke(reqObject,manage,checkReqObj,DELETE_METHOD);
            if (object instanceof Integer){
                if (((Integer)object).intValue() !=0) {
                    return BeanMapperUtil.objConvert(reqObject,resClass);
                }
            }
            throw new ManageException(Constants.SERVICE_DELETE_FAIL_FLAG, Constants.SERVICE_DELETE_FAIL_MSG);
        } catch (Exception e) {
            log.error("ServiceUtil调用DELETE方法异常：{}",e.getClass().getName());
            throw e;
        }
    }

    /**
     * Service层调用Manage层工具——改
     * @param reqObject Request Data Transaction Object
     * @param manage Manage Interface Bean
     * @param resClass Response Data Transaction Object的Class类
     * @return 返回Service层的ResDTO
     */
    public static <T> T updateByKey(Object reqObject, String key, Object manage, Class<T> resClass, boolean checkReqObj) throws Exception {
        try {
            log.info("ServiceUtil调用UPDATE方法 RequestDTO参数：{}",reqObject);
            //反射函数
            Object object = invoke(reqObject,manage,checkReqObj,UPDATE_METHOD,key);
            if (object instanceof Integer){
                if (((Integer)object).intValue() !=0) {
                    return BeanMapperUtil.objConvert(reqObject,resClass);
                }
            }
            throw new ManageException(Constants.SERVICE_UPDATE_FAIL_FLAG, Constants.SERVICE_UPDATE_FAIL_MSG);
        } catch (Exception e) {
            log.error("ServiceUtil调用UPDATE方法异常：{}",e.getClass().getName());
            throw e;
        }
    }

    /**
     * Service层调用Manage层工具——查
     * @param reqObject Request Data Transaction Object
     * @param manage Manage Interface Bean
     * @param resClass Response Data Transaction Object的Class类
     * @return 返回Service层的ResDTO
     */
    public static <T> List<T> select(Object reqObject, Object manage, Class<T> resClass, boolean checkReqObj) throws Exception {
        try {
            log.info("ServiceUtil调用SELECT方法 RequestDTO参数：{}",reqObject);
            //反射函数
            Object object = invoke(reqObject,manage,checkReqObj,SELECT_METHOD);
            if (object instanceof List) {
                List<T> list = (List<T>)object;
                List<T> resultList = new ArrayList<>();
                if (list.size() != 0) {
                    for (Object tempBO:list) {
                        resultList.add(BeanMapperUtil.objConvert(tempBO,resClass));
                    }
                    return resultList;
                }
            }
            throw new ManageException(Constants.SERVICE_SELECTED_FAIL_FLAG, Constants.SERVICE_SELECTED_FAIL_MSG);
        } catch (Exception e) {
            log.error("ServiceUtil调用SELECT方法异常：{}",e.getClass().getName());
            throw e;
        }
    }

    /**
     * Service层调用Manage层工具——单一查询
     * @param reqObject Request Data Transaction Object
     * @param manage Manage Interface Bean
     * @param resClass Response Data Transaction Object的Class类
     * @return 返回Service层的ResDTO
     */
    public static <T> T selectSingle(Object reqObject, Object manage, Class<T> resClass, boolean checkReqObj) throws Exception {
        try {
            log.info("ServiceUtil调用SELECT_SINGLE方法 RequestDTO参数：{}",reqObject);
            //反射函数
            Object object = invoke(reqObject,manage,checkReqObj,SELECT_METHOD);
            if (object instanceof List && ((List)object).size() == 1) {
                return BeanMapperUtil.objConvert(((List)object).get(0),resClass);
            }
            throw new ManageException(Constants.SERVICE_SELECTED_FAIL_FLAG, Constants.SERVICE_SELECTED_FAIL_MSG);
        } catch (Exception e) {
            log.error("ServiceUtil调用SELECT_SINGLE方法异常：{}",e.getClass().getName());
            throw e;
        }
    }

    /**
     * Service层调用Manage层工具——增
     */
    public static <T> T insert(Object reqObject, Object manage, Class<T> resClass) throws Exception {
        return insert(reqObject,manage,resClass,true);
    }

    /**
     * Service层调用Manage层工具——删
     */
    public static <T> T delete(Object reqObject, Object manage, Class<T> resClass) throws Exception {
        return delete(reqObject,manage,resClass,true);
    }

    /**
     * Service层调用Manage层工具——改
     */
    public static <T> T updateByKey(Object reqObject, String key, Object manage, Class<T> resClass) throws Exception {
        return updateByKey(reqObject,key,manage,resClass,true);
    }

    /**
     * Service层调用Manage层工具——查
     */
    public static <T> List<T> select(Object reqObject, Object manage, Class<T> resClass) throws Exception {
        return select(reqObject,manage,resClass,true);
    }

    /**
     * Service层调用Manage层工具——单一查询
     */
    public static <T> T selectSingle(Object reqObject, Object manage, Class<T> resClass) throws Exception {
        return selectSingle(reqObject,manage,resClass,true);
    }

    /**
     * Service层调用Manage层工具——查全表
     * @param manage Manage Interface Bean
     * @param resClass Response Data Transaction Object的Class类
     * @return 返回Service层的ResDTO
     */
    public static <T> List<T> selectAll(Object manage, Class<T> resClass) throws Exception {
        try {
            log.info("ServiceUtil调用SELECT方法 Respnse返回类型：{}",resClass.getName());
            Object object = invoke(null,manage,false,SELECT_ALL_METHOD);
            if (object instanceof List) {
                return BeanMapperUtil.objConvert((List)object,resClass);
            }
            throw new ManageException(Constants.SERVICE_SELECTED_FAIL_FLAG, Constants.SERVICE_SELECTED_FAIL_MSG);
        } catch (Exception e) {
            log.error("ServiceUtil调用SELECTALL方法异常：{}",e.getClass().getName());
            throw e;
        }
    }

    /**
     * 通过
     *
     * @param requestDTOClazz
     * @return
     * @throws ClassNotFoundException
     */
    private static Class getBusinessObject(Class requestDTOClazz) throws ClassNotFoundException{
        String clazzName = requestDTOClazz.getName().replace("ReqDTO","BO").replace("service.model.request","manage.model");
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            log.error("ServiceUtil反射异常：无法根据{}类找到对应的{}类 Exception名称：{}",requestDTOClazz.getName(),clazzName,e.getClass().getName());
            throw e;
        }
    }

    /**
     * 通过Manage获得BO模型
     *
     * @param manage ManageImpl实体类
     * @return BO 模型
     * @throws ClassNotFoundException
     */
    private static Class getBusinessObject(Object manage) throws ClassNotFoundException {
        Class implClazz = ReflectionUtil.getTargetClazz(manage);
        String clazzName = implClazz.getName().replace("ManageImpl", "BO").replace("manage.impl", "manage.model");
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            log.error("ServiceUtil反射异常：无法根据{}类找到对应的{}类 Exception名称：{}", implClazz.getName(), clazzName, e.getClass().getName());
            throw e;
        }
    }

    /**
     * 根据ManageBean分割表名
     * @param manage Service Bean
     */
    private static String splitTableName(Object manage) {
        Class clazz = ReflectionUtil.getTargetClazz(manage);
        String[] strings = clazz.getName().replace("ManageImpl","").split("\\.");
        return strings[strings.length-1];
    }

}
