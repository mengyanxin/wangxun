package com.mengyan.xin.web.controller.user;

import com.mengyan.xin.dal.model.LogicDO;
import com.mengyan.xin.manage.utils.BeanMapperUtil;
import com.mengyan.xin.service.base.user.UserLoginService;
import com.mengyan.xin.service.model.request.user.UserLoginReqDTO;
import com.mengyan.xin.web.model.request.user.UserLoginReqVO;
import com.mengyan.xin.web.model.response.user.UserLoginResVO;
import com.mengyan.xin.web.utils.AjaxInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mengyanxin on 2017/5/3.
 */
@Controller
@Slf4j
@RequestMapping(value = "/user/login/")
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    @RequestMapping(value = "insert")
    public void insert(UserLoginReqVO userLoginReqVO, HttpServletRequest request, HttpServletResponse response){
        try {
            userLoginService.insert(BeanMapperUtil.objConvert(userLoginReqVO, UserLoginReqDTO.class));
            AjaxInfoUtil.setSuccessResponse(BeanMapperUtil.objConvert(userLoginReqVO,UserLoginResVO.class),response);
        }catch (Exception e){
            AjaxInfoUtil.setFailureResponse(e,response);
        }
    }

    @RequestMapping(value = "update")
    public void update(UserLoginReqVO userLoginReqVO, HttpServletRequest request, HttpServletResponse response){
        try {
            LogicDO logicDO = new LogicDO();
            logicDO.setKey("id");
            userLoginService.update(BeanMapperUtil.objConvert(userLoginReqVO, UserLoginReqDTO.class),logicDO);
            AjaxInfoUtil.setSuccessResponse(BeanMapperUtil.objConvert(userLoginReqVO,UserLoginResVO.class),response);
        }catch (Exception e){
            AjaxInfoUtil.setFailureResponse(e,response);
        }
    }

    @RequestMapping(value = "delete")
    public void delete(UserLoginReqVO userLoginReqVO, HttpServletRequest request, HttpServletResponse response){
        try {
            userLoginService.delete(BeanMapperUtil.objConvert(userLoginReqVO, UserLoginReqDTO.class));
            AjaxInfoUtil.setSuccessResponse(BeanMapperUtil.objConvert(userLoginReqVO,UserLoginResVO.class),response);
        }catch (Exception e){
            AjaxInfoUtil.setFailureResponse(e,response);
        }
    }

    @RequestMapping(value = "select")
    public void select(UserLoginReqVO userLoginReqVO, HttpServletRequest request, HttpServletResponse response){
        try {
            AjaxInfoUtil.setSuccessResponse(userLoginService.select(BeanMapperUtil.objConvert(userLoginReqVO, UserLoginReqDTO.class),new LogicDO()),response);
        }catch (Exception e){
            AjaxInfoUtil.setFailureResponse(e,response);
        }
    }

    @RequestMapping(value = "select/all")
    public void selectAll(UserLoginReqVO userLoginReqVO, HttpServletRequest request, HttpServletResponse response){
        try {
            AjaxInfoUtil.setSuccessResponse(userLoginService.selectAll(new LogicDO()),response);
        }catch (Exception e){
            AjaxInfoUtil.setFailureResponse(e,response);
        }
    }
}
