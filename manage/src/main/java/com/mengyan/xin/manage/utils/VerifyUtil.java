package com.mengyan.xin.manage.utils;

import com.mengyan.xin.dal.model.LogicDO;
import lombok.extern.slf4j.Slf4j;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * Oval校验工具类
 */
@Slf4j
public class VerifyUtil {

    private static Validator validator = new Validator();

    public static LogicDO validateLogic(LogicDO logicDO,int offset) throws ManageException{
        if(logicDO == null){
            logicDO = new LogicDO();
            logicDO.setPage(1);
        }
        if(logicDO.getPage()==null || logicDO.getPage()<=1){
            logicDO.setPage(1);
        }
        logicDO.setOffset(offset);
        logicDO.setIndex((logicDO.getPage()-1)*logicDO.getOffset());
        return logicDO;
    }

    /**
     * 验证当前用户是否已登陆
     * @param request
     * @return
     * @throws ManageException
     */
    public static void validateUser(HttpServletRequest request) throws ManageException{
        //获取session中的用户id
        Object userId = request.getSession().getAttribute(Constants.SESSION_USER_ID);
        if (userId==null||"".equals(userId)){
            throw new ManageException(Constants.SESSION_LOGIN_FLAG, Constants.SESSION_LOGIN_MSG);
        }
    }

    /**
     * 请求参数非空、格式验证，请求对象
     * @param object 请求校验参数
     */
    public static void validateObject(Object object) throws ManageException {
        List<ConstraintViolation> list = validator.validate(object);
        if (null != list && !list.isEmpty()) {
            throw new ManageException(Constants.PARAMETER_ERROR_FLAG, list.get(0).getMessage());
        }
    }

    /**
     * 校验参数是否非空
     * @param object 待验证对象
     * @throws Exception 参数为空
     */
    public static void validateNull(Object object) throws Exception {
        if (object == null) {
            log.error("验证{}类型参数为空异常", object.toString());
            throw new Exception("验证Object类型参数为空异常");
        }
    }

    /**
     * Object内是否所有参数为空
     *
     * @param object 可以对ReqDTO检查
     * @return
     * @throws Exception
     */
    public static boolean allFieldsNull(Object object) throws Exception {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            field.setAccessible(true);
            Object value = field.get(object);
            if (value != null) {
                if (!value.toString().trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 字符串参数是否非空
     * @param params 待验证参数
     * @throws Exception 参数为空
     */
    public static void validateStringNull(String... params) throws Exception {
        for (String param : params) {
            if (StringUtils.isEmpty(param)) {
                log.error("验证String类型参数为空异常");
                throw new Exception("验证Object类型参数为空异常");
            }
        }
    }

    /**
     * 检查数据库记录操作数
     * @param updNum 更新数
     */
    public static void checkUpdNum(int updNum) throws Exception {
        if (updNum != 1) {
            log.error("记录更新数:{} ,update num is not one", updNum);
            throw new Exception();
        }
    }

    /**
     * 字符串参数长度校验
     * @param strings 待验证参数
     * @throws Exception
     */
    public static void validateStringMaxLength(int maxLength, String... strings) throws Exception {
        validateStringLength(Integer.MIN_VALUE,maxLength,strings);
    }

    public static void validateStringMinLength(int minLength, String... strings) throws Exception {
        validateStringLength(minLength,Integer.MAX_VALUE,strings);
    }

    public static void validateStringLength(int minLength, int maxLength, String... strings) throws Exception {
        for (String param:strings) {

            if (StringUtils.isNotEmpty(param) && param.length() > maxLength) {
                log.error("参数长度超限");
                throw new Exception();
            }

            if (StringUtils.isNotEmpty(param) && param.length() < minLength) {
                log.error("参数长度超限");
                throw new Exception();
            }
        }
    }


    /**
     * 检查时间是否为空,为空则添加当前时间
     * @param date 传入的时间
     */
    public static Date initDateIfNull(Date date) throws Exception {
        try {
            validateNull(date);
        } catch (Exception e) {
            date = new Date();
        } finally {
            return date;
        }
    }

}


