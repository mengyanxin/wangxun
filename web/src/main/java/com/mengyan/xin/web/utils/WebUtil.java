package com.mengyan.xin.web.utils;

import com.mengyan.xin.manage.utils.BeanMapperUtil;
import com.mengyan.xin.manage.utils.ExceptionUtil;
import com.mengyan.xin.manage.utils.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.aop.support.AopUtils;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 网络层工具——通过反射调用服务层通用函数
 */
@Slf4j
public class WebUtil {

    private final static String INSERT_METHOD = "insert";
    private final static String DELETE_METHOD = "delete";
    private final static String UPDATE_METHOD = "update";
    private final static String SELECT_METHOD = "select";
    private final static String SELECT_ALL_METHOD = "selectAll";
    private final static BeanMappingBuilder configure = new BeanMappingBuilder() {
        @Override
        protected void configure() {

        }
    };

    /**
     * 这里使用Reflection工具是因为不确定Service的方法会带几个参数(比如UpdateByKey)
     */
    private static Object invoke(Object requestVO, Object service, String... methodNameAndArgs) throws Exception {
        //方法名
        String methodName = methodNameAndArgs[0] + splitTableName(service);
        try {
            //拼接参数
            Class objectReqDTOClazz = getRequestDTOClazz(requestVO.getClass());
            Object objectReqDTO = BeanMapperUtil.objConvert(requestVO,objectReqDTOClazz);
            Object[] args = new Object[methodNameAndArgs.length];
            args[0] = objectReqDTO;
            for (int i = 1;i < args.length;i++) {
                args[i] = methodNameAndArgs[i];
            }
            //找到方法
            Method method = ReflectionUtil.methodByNameAndArgsInClass(service.getClass(),methodName,args);
            method.setAccessible(true);
            //触发函数
            Object responseDTO = method.invoke(service,args);
            //转化为ResponseVO
            Class<?> responseVOClazz = getResponseVOClazz(requestVO.getClass());
            Object responseVO;
            if (responseDTO instanceof List) {
                responseVO = BeanMapperUtil.objConvert((List) responseDTO, responseVOClazz, configure);
            }else {
                responseVO = BeanMapperUtil.objConvert(responseDTO, responseVOClazz, configure);
            }
            return responseVO;
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (NoSuchMethodException e) {
            log.error("WebUtil无法找到反射方法{}: {}",methodName,e.getClass().getName());
            throw e;
        } catch (IllegalAccessException e) {
            log.error("WebUtil反射方法{} Access异常: {}",methodName,e.getClass().getName());
            throw e;
        } catch (InvocationTargetException e) {
            String exceptionName = e.getClass().getName();
            String targetName = ExceptionUtil.getInvocationTarget(e).getClass().getName();
            log.error("WebUtil反射方法{} Invoke异常: {} Target异常: {}",methodName,exceptionName,targetName);
            throw e;
        }
    }

    /**
     * Web层反射工具——增
     * @param requestVO Request Value Object
     * @param service Service Bean
     */
    public static void insert(Object requestVO, Object service, HttpServletResponse response) {
        try {
            log.info("WebUtil调用INSERT方法 requestVO参数：{}",requestVO);
            Object object = invoke(requestVO,service,INSERT_METHOD);
            if (object != null) {
                AjaxInfoUtil.setSuccessResponse(object,response);
            }
        } catch (Exception e) {
            log.error("WebUtil调用INSERT方法异常：{}",e.getClass().getName());
            AjaxInfoUtil.setFailureResponse(e, response);
        }
    }

    /**
         * Web层反射工具——删
         * @param requestVO Request Value Object
         * @param service Service Bean
         */
    public static void delete(Object requestVO, Object service, HttpServletResponse response) {
        try {
            log.info("WebUtil调用DElETE方法 RequestVO参数：{}",requestVO);
            Object object = invoke(requestVO,service,DELETE_METHOD);
            if (object != null) {
                AjaxInfoUtil.setSuccessResponse(object,response);
            }
        } catch (Exception e) {
            log.error("WebUtil调用DELETE方法异常：{}",e.getClass().getName());
            AjaxInfoUtil.setFailureResponse(e, response);
        }
    }

    /**
     * Web层反射工具——改
     * @param requestVO Request Value Object
     * @param service Service Bean
     */
    public static void update(Object requestVO, String key, Object service, HttpServletResponse response) {
        try {
            log.info("WebUtil调用UPDATE方法 RequestVO参数：{}",requestVO);
            Object object = invoke(requestVO,service,UPDATE_METHOD,key);
            if (object != null) {
                AjaxInfoUtil.setSuccessResponse(object,response);
            }
        } catch (Exception e) {
            log.error("WebUtil调用UPDATE方法异常：{}",e.getClass().getName());
            AjaxInfoUtil.setFailureResponse(e, response);
        }
    }

    /**
         * Web层反射工具——查
         * @param requestVO Request Value Object
         * @param service Service Bean
         */
    public static void select(Object requestVO, Object service, HttpServletResponse response) {
        try {
            log.info("WebUtil调用SELECT方法 RequestVO参数：{}",requestVO);
            Object object = invoke(requestVO,service,SELECT_METHOD);
            if (object != null) {
                AjaxInfoUtil.setSuccessResponse(object,response);
            }
        } catch (Exception e) {
            log.error("WebUtil调用SELECT方法异常：{}",e.getClass().getName());
            AjaxInfoUtil.setFailureResponse(e, response);
        }
    }

    /**
     * Web层反射工具——查全部Ajax返回
     * @param service Service Bean
     */
    public static <T> void selectAll(Object service, Class<T> resClazz, HttpServletResponse response) {
        try {
            log.info("WebUtil调用SELECT方法 Respnse返回类型：{}",resClazz.getName());
            List<T> list = selectAll(service,resClazz);
            AjaxInfoUtil.setSuccessResponse(list,response);
        } catch (Exception e) {
            log.error("WebUtil调用SELECTALL方法异常：{}",e.getClass().getName());
            AjaxInfoUtil.setFailureResponse(e,response);
        }
    }

    /**
     * Web层反射工具——查全部
     * @param service Service Bean
     */
    public static <T> List<T> selectAll(Object service, Class<T> resClazz) throws Exception {
        List<T> list;
        String tableName = splitTableName(service);
        String methodName = SELECT_ALL_METHOD + tableName;
        try {
            //找到方法
            Method method = ReflectionUtil.methodByNameAndArgsInClass(service.getClass(),methodName);
            method.setAccessible(true);
            //触发函数
            Object responseDTO = method.invoke(service);
            list = BeanMapperUtil.objConvert((List) responseDTO, resClazz, configure);
        } catch (NoSuchMethodException e) {
            log.error("WebUtil无法找到反射方法{}: {}",methodName,e.getClass().getName());
            throw e;
        } catch (IllegalAccessException e) {
            log.error("WebUtil反射方法{} Access异常: {}",methodName,e.getClass().getName());
            throw e;
        } catch (InvocationTargetException e) {
            log.error("WebUtil反射方法{} Invoke异常: {}",methodName,e.getClass().getName());
            throw e;
        }
        return list;
    }

    /**
     * 直接执行Service函数并以Ajax返回
     */
    public static void ajaxJSONServiceMethod(Object requestVO, Object service, HttpServletResponse response, String methodName) {
        try {
            log.info("WebUtil调用Service层{}方法 RequestVO参数：{}",methodName,requestVO);
            Class requestDTOClazz = getRequestDTOClazz(requestVO.getClass());
            Object requestDTO = BeanMapperUtil.objConvert(requestVO,requestDTOClazz);
            Method method = service.getClass().getDeclaredMethod(methodName,requestDTOClazz);
            method.setAccessible(true);
            Object responseDTO = method.invoke(service,requestDTO);
            Class responseVOClazz = getResponseVOClazz(requestVO.getClass());
            Object reponseVO;
            if (responseDTO instanceof List) {
                reponseVO = BeanMapperUtil.objConvert((List) responseDTO, responseVOClazz, configure);
            } else {
                reponseVO = BeanMapperUtil.objConvert(responseDTO, responseVOClazz, configure);
            }
            AjaxInfoUtil.setSuccessResponse(reponseVO,response);
        } catch (Exception e) {
            log.error("WebUtil调用Service层{}方法异常类型: {} 异常信息: {} RequestVO参数：{}",methodName,e.getClass().getName(),e.getMessage(),requestVO);
            AjaxInfoUtil.setFailureResponse(e,response);
        }
    }

    /**
     * 获得请求业务模型
     * @param requestVOClazz
     */
    private static Class getRequestDTOClazz(Class requestVOClazz) throws ClassNotFoundException {
        String requestDTOName = requestVOClazz.getName().replace("VO","DTO").replace("web","service");
        try {
            return Class.forName(requestDTOName);
        } catch (ClassNotFoundException e) {
            log.error("WebUtil反射异常：无法根据{}类找到对应的{}类 Exception名称：{}",requestVOClazz.getName(),requestDTOName,e.getClass().getName());
            throw e;
        }
    }

    /**
     * 获得返回值模型
     * @param requestVOClazz
     */
    private static Class getResponseVOClazz(Class requestVOClazz) throws ClassNotFoundException {
        String responseVOName = requestVOClazz.getName().replace("Req","Res").replace("request","response");
        try {
            return Class.forName(responseVOName);
        } catch (ClassNotFoundException e) {
            log.error("WebUtil反射异常：无法根据{}类找到对应的{}类 Exception名称：{}",requestVOClazz.getName(),responseVOName,e.getClass().getName());
            throw e;
        }
    }

    /**
     * 根据ServiceBean分割表名
     * @param service Service Bean
     */
    private static String splitTableName(Object service) {
        Class clazz;
        if (AopUtils.isAopProxy(service)) {
            clazz = AopUtils.getTargetClass(service);
        }else {
            clazz = service.getClass();
        }
        String[] strings = clazz.getName().replace("ServiceImpl","").split("\\.");
        return strings[strings.length-1];
    }


}
