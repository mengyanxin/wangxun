package com.mengyan.xin.manage.utils;

import com.mengyan.xin.dal.model.LogicDO;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理层工具——通过反射调用DAL层通用函数
 */
@Slf4j
public class ManageUtil {

    private final static String INSERT_METHOD = "insert";
    private final static String DELETE_METHOD = "delete";
    private final static String UPDATE_BY_PRIMARYKEY_METHOD = "updateByPrimaryKey";
    private final static String SELECT_METHOD = "select";
    private final static String SELECT_BY_KEY_METHOD = "selectByKey";
    private final static String SELECT_BY_SEARCH_KEY_OR_METHOD = "selectBySearchKeyOr";
    private final static String SELECT_BY_SEARCH_KEY_AND_METHOD = "selectBySearchKeyAnd";
    private final static String SELECT_ALL_METHOD = "selectAll";



    private static Object invoke(Object objectBO, Object mapper, Object... methodNameAndArgs) throws Exception {
//            Object target = ReflectionUtil.getTarget(mapper);
        try {
            Object[] args;
            //根据是否传入ObjectBO拼接参数
            if (objectBO != null) {
                //获得方法和数据模型
                Class objectDOClazz = getDataObjectClazz(objectBO.getClass());
                Object objectDO = BeanMapperUtil.objConvert(objectBO, objectDOClazz);
                //组合参数
                args = new Object[methodNameAndArgs.length];
                args[0] = objectDO;
                for (int i =1 ; i < args.length ;i++) {
                    args[i] = methodNameAndArgs[i];
                }
            } else {
                args = new Object[methodNameAndArgs.length - 1];
                for (int i =0 ; i < args.length ;i++) {
                    args[i] = methodNameAndArgs[i+1];
                }
            }
            Method method = ReflectionUtil.methodByNameAndArgsInClass(mapper.getClass(),methodNameAndArgs[0].toString(),args);
            method.setAccessible(true);
            //触发函数
            return method.invoke(mapper,args);
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (NoSuchMethodException e) {
            log.error("ManageUtil无法找到反射方法{}: {}",methodNameAndArgs[0],e.getClass().getName());
            e.printStackTrace();
            throw e;
        } catch (IllegalAccessException e) {
            log.error("ManageUtil反射方法{} Access异常: {}",methodNameAndArgs[0],e.getClass().getName());
            throw e;
        } catch (InvocationTargetException e) {
            String exceptionName = e.getClass().getName();
            String targetName = ExceptionUtil.getInvocationTarget(e).getClass().getName();
            e.printStackTrace();
            log.error("ManageUtil反射方法{} Invoke异常: {} Target异常: {}",methodNameAndArgs[0],exceptionName,targetName);
            throw e;
        }
    }


    /**
     * Manger层数据库基本工具——增
     * @param objectBO 接收到的Business Object
     * @param mapper 使用的MyBatisDOMapper实例
     * @return 返回Insert行数
     */
    public static int insert(Object objectBO, Object mapper) throws Exception {
        try {
            log.info("ManageUtil调用INSERT方法 BO参数：{}",objectBO);
            Object object = invoke(objectBO, mapper, INSERT_METHOD);
            if (object instanceof Integer){
                return ((Integer)object).intValue();
            }
            throw new ManageException(Constants.MANAGE_INSERT_FAIL_FLAG,Constants.MANAGE_INSERT_FAIL_MSG);
        } catch (Exception e) {
            log.error("ManageUtil调用INSERT方法异常：{}",e.getClass().getName());
            throw e;
        }
    }
    /**
     * 插入后根据主键生成业务ID
     * @param objectBO
     * @param mapper
     * @param prefix
     */
    public static int insertWithIdPrefix(Object objectBO, Object mapper, String prefix) throws Exception {

        try {
            insert(objectBO,mapper);
            String primaryKey = "id";
            //获得主ID之后取出来
            Class clazzDO = getDataObjectClazz(objectBO.getClass());
            Object objectDO = selectSingle(objectBO,mapper,clazzDO);
            Method selectMethod = mapper.getClass().getMethod(SELECT_METHOD,clazzDO,LogicDO.class);
            Object obj = new LogicDO();
            List list = (List)selectMethod.invoke(mapper,objectDO,obj);
            if (list.size() == 1) {
                objectDO = list.get(0);
            }
            Field[] fields = objectDO.getClass().getDeclaredFields();
            Field DOIdField = objectDO.getClass().getDeclaredField(primaryKey);
            DOIdField.setAccessible(true);
            //加前缀
            for (int i = 0; i < fields.length; i ++) {
                Field field  = fields[i];
                field.setAccessible(true);
                String name = field.getName();
                if (name.contains("Id") && field.get(objectDO) == null) {
                    //同时更新输入的数据
                    Field BOField = objectBO.getClass().getDeclaredField(field.getName());
                    BOField.setAccessible(true);
                    //组合业务ID
                    String combineId = prefix + DOIdField.get(objectDO);
                    Class clazz = field.getType();
                    if (clazz.equals(Integer.class)) {
                        field.set(objectDO,Integer.parseInt(combineId));
                        BOField.set(objectBO,Integer.parseInt(combineId));
                        break;
                    }
                    if (clazz.equals(String.class)) {
                        field.set(objectDO,combineId);
                        BOField.set(objectBO,combineId);
                        break;
                    }
                    throw new Exception();
                }
            }
            //通过主键更新
            Object num = invoke(objectDO,mapper,UPDATE_BY_PRIMARYKEY_METHOD);
            if (num instanceof Integer) {
                return (int)num;
            }
            throw new Exception();
        } catch (ManageException e) {
            boolean duplicateUsername = e.getMessage().contains("Duplicate entry");
            if (duplicateUsername) {
                throw new ManageException(e.getErrorCode(), Constants.MANAGE_INSERT_MSG_DUPLICATE);
            }
            throw e;
        } catch (Exception e) {
            throw new ManageException(Constants.MANAGE_INSERT_FAIL_FLAG,e.getMessage());
        }
    }
    /**
     * Manger层数据库基本工具——删
     * @param objectBO 接收到的Business Object
     * @param mapper 使用的MyBatisDOMapper实例
     * @return 返回Insert行数
     */
    public static int delete(Object objectBO, Object mapper) throws Exception {
        try {
            log.info("ManageUtil调用DELETE方法 BO参数：{}",objectBO);
            Object object = invoke(objectBO, mapper, DELETE_METHOD);
            if (object instanceof Integer){
                return ((Integer)object).intValue();
            }
            throw new ManageException(Constants.MANAGE_DELETE_FAIL_FLAG,Constants.MANAGE_DELETE_FAIL_MSG);
        } catch (Exception e) {
            log.error("ManageUtil调用DELETE方法异常：{}",e.getClass().getName());
            throw e;
        }
    }

    /**
     * Manger层数据库基本工具——假删除
     *
     * @param objectBO 接收到的Business Object
     * @param mapper   使用的MyBatisDOMapper实例
     * @return 返回删除行数
     */
    public static int fakeDelete(Object objectBO, Object mapper) throws Exception {
        Object objectDO = selectSingle(objectBO, mapper, getDataObjectClazz(objectBO.getClass()));
        Field deleteFiled = objectDO.getClass().getDeclaredField("isDelete");
        deleteFiled.setAccessible(true);
        deleteFiled.set(objectDO, "y");
        Object fake = invoke(objectDO, mapper, UPDATE_BY_PRIMARYKEY_METHOD);
        return (int) fake;
    }
    /**
     * Manger层数据库基本工具——改
     * @param objectBO 接收到的Business Object
     * @param mapper 使用的MyBatisDOMapper实例
     * @return 返回Insert行数
     */
    public static int updateByKey(Object objectBO, String key, Object mapper) throws Exception {
        try {
            log.info("ManageUtil调用UPDATE方法 BO参数：{}",objectBO);
            LogicDO logicDO = new LogicDO();
            logicDO.setKey(key);
            Object object = invoke(objectBO, mapper, SELECT_BY_KEY_METHOD, logicDO);
            if (object instanceof List) {
                List<Object> list = (List<Object>) object;
                List<Object> resultList = new ArrayList<>();
                for (Object tempDO:list) {
                    BeanMapperUtil.objUpdate(objectBO,tempDO);
                    //使用新模型去更新
                    invoke(tempDO,mapper,UPDATE_BY_PRIMARYKEY_METHOD);
                    resultList.add(tempDO);
                }
                return resultList.size();
            }
            throw new ManageException(Constants.MANAGE_UPDATE_FAIL_FLAG,Constants.MANAGE_UPDATE_FAIL_MSG);
        } catch (Exception e) {
            log.error("ManageUtil调用UPDATE方法异常：{}",e.getClass().getName());
            throw e;
        }
    }
    /**
     * Manger层数据库基本工具——查
     * @param objectBO 接收到的Business Object
     * @param mapper 使用的MyBatisDOMapper实例
     * @return 返回Insert行数
     */
    public static <T> List<T> select(T objectBO, Object mapper) throws Exception {
        try {
            log.info("ManageUtil调用SELECT方法 BO参数：{}",objectBO);
            LogicDO obj = new LogicDO();
            Object object = invoke(objectBO, mapper, SELECT_METHOD,obj);
            if (object instanceof List) {
                List<T> list = (List<T>) object;
                List<T> resultList = new ArrayList<>();
                for (Object tempDO:list) {
                    resultList.add((T) BeanMapperUtil.objConvert(tempDO,objectBO.getClass()));
                }
                if (resultList.size() != 0) {
                    return resultList;
                }
            }
            throw new ManageException(Constants.MANAGE_SELECTED_FAIL_FLAG,Constants.MANAGE_SELECTED_FAIL_MSG);
        } catch (Exception e) {
            log.error("ManageUtil调用SELECT方法异常：{}",e.getClass().getName());
            throw e;
        }
    }
    /**
     * Manger层数据库基本工具——查
     * @param objectBO 接收到的Business Object
     * @param mapper 使用的MyBatisDOMapper实例
     * @return 返回单一模型
     */
    public static <T> T selectSingle(Object objectBO, Object mapper, Class<T> classDO) throws Exception {
        try {
            log.info("ManageUtil调用SELECT_SINGLE方法 BO参数：{}",objectBO);
            LogicDO obj = new LogicDO();
            Object object = invoke(objectBO, mapper, SELECT_METHOD,obj);
            if (object instanceof List) {
                List<T> list = ((List<T>)object);
                if (list.get(0).getClass().equals(classDO) && list.size() == 1 && list != null) {
                    return list.get(0);
                }
            }
            throw new ManageException(Constants.MANAGE_SELECTED_FAIL_FLAG,Constants.MANAGE_SELECTED_FAIL_MSG);
        } catch (Exception e) {
            log.error("ManageUtil调用SELECT_SINGLE方法异常：{}",e.getClass().getName());
            throw e;
        }
    }
    /**
     * Manger层数据库基本工具——查全表
     * @param mapper 使用的MyBatisDOMapper实例
     * @return
     */
    public static <T> List<T> selectAll(Object mapper,Class<T> classBO) throws Exception {
        try {
            log.info("ManageUtil调用SELECT方法 Respnse返回类型：{}",classBO.getName());
            Object object = invoke(null,mapper,SELECT_ALL_METHOD);
            if (object instanceof List) {
                return BeanMapperUtil.objConvert((List)object,classBO);
            }
            throw new ManageException(Constants.MANAGE_SELECTED_FAIL_FLAG,Constants.MANAGE_SELECTED_FAIL_MSG);
        } catch (Exception e) {
            log.error("ManageUtil调用SELECTALL方法异常：{}",e.getClass().getName());
            throw e;
        }
    }

    /**
     * @param objectBO 包含查询关键词的BO模型
     * @param mapper   使用哪个Mapper查询
     * @param <T>      查询BO模型的Class
     * @return
     * @throws Exception
     */
    public static <T> List<T> selectBySearchKey(T objectBO, Object mapper, LogicDO logicDO) throws Exception {
        try {
            log.info("ManageUtil调用SELECT_BY_SEARCH_KEY方法 BO参数：{}", objectBO);
            String methodName = logicDO.getKey() != null && logicDO.getKey().equals("or") ? SELECT_BY_SEARCH_KEY_OR_METHOD : SELECT_BY_SEARCH_KEY_AND_METHOD;
            Object object = invoke(objectBO, mapper, methodName, logicDO);
            if (object instanceof List) {
                List<T> list = (List<T>) object;
                List<T> resultList = new ArrayList<>();
                for (Object tempDO : list) {
                    resultList.add((T) BeanMapperUtil.objConvert(tempDO, objectBO.getClass()));
                }
//                if (resultList.size() != 0) {
                    return resultList;
//                }
            }
            throw new ManageException(Constants.MANAGE_SELECTED_FAIL_FLAG, Constants.MANAGE_SELECTED_FAIL_MSG);
        } catch (Exception e) {
            log.error("ManageUtil调用SELECT_BY_SEARCH_KEY方法异常：{}", e.getClass().getName());
            throw e;
        }
    }
    /**
     * 通过BO模型获得DO模型
     * @param objectBOClazz BO模型类
     * @return DO模型类
     * @throws ClassNotFoundException
     */
    private static Class getDataObjectClazz(Class objectBOClazz) throws ClassNotFoundException {
        String clazzName = objectBOClazz.getName().replace("BO","DO").replace("manage","dal");
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            log.error("ManageUtil反射异常：无法根据{}类找到对应的{}类 Exception名称：{}",objectBOClazz.getName(),clazzName,e.getClass().getName());
            throw e;
        }
    }
}
