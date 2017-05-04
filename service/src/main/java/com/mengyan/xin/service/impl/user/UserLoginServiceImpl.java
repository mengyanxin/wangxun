package com.mengyan.xin.service.impl.user;

import com.mengyan.xin.dal.model.LogicDO;
import com.mengyan.xin.manage.base.user.UserLoginManage;
import com.mengyan.xin.manage.model.user.UserLoginBO;
import com.mengyan.xin.manage.utils.BeanMapperUtil;
import com.mengyan.xin.manage.utils.Constants;
import com.mengyan.xin.manage.utils.ManageException;
import com.mengyan.xin.service.base.user.UserLoginService;
import com.mengyan.xin.service.model.request.user.UserLoginReqDTO;
import com.mengyan.xin.service.model.response.user.UserLoginResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mengyanxin on 2017/5/3.
 */
@Slf4j
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserLoginManage userLoginManage;

    /**
     * 新增
     *
     * @param userLoginReqDTO
     * @return
     * @throws Exception
     */
    @Override
    public UserLoginResDTO insert(UserLoginReqDTO userLoginReqDTO) throws Exception {
        if(userLoginManage.insert(BeanMapperUtil.objConvert(userLoginReqDTO, UserLoginBO.class))>0){
            return BeanMapperUtil.objConvert(userLoginReqDTO,UserLoginResDTO.class);
        }
        throw new ManageException(Constants.SERVICE_INSERT_FAIL_FLAG, Constants.SERVICE_INSERT_FAIL_MSG);
    }

    /**
     * 删除
     *
     * @param userLoginReqDTO
     * @return
     * @throws Exception
     */
    @Override
    public UserLoginResDTO delete(UserLoginReqDTO userLoginReqDTO) throws Exception {
        if(userLoginManage.delete(BeanMapperUtil.objConvert(userLoginReqDTO, UserLoginBO.class))>0){
            return BeanMapperUtil.objConvert(userLoginReqDTO,UserLoginResDTO.class);
        }
        throw new ManageException(Constants.SERVICE_INSERT_FAIL_FLAG, Constants.SERVICE_INSERT_FAIL_MSG);
    }

    /**
     * 更新
     *
     * @param userLoginReqDTO
     * @param logicDO
     * @return
     * @throws Exception
     */
    @Override
    public UserLoginResDTO update(UserLoginReqDTO userLoginReqDTO, LogicDO logicDO) throws Exception {
        if(userLoginManage.update(BeanMapperUtil.objConvert(userLoginReqDTO, UserLoginBO.class),logicDO)>0){
            return BeanMapperUtil.objConvert(userLoginReqDTO,UserLoginResDTO.class);
        }
        throw new ManageException(Constants.SERVICE_UPDATE_FAIL_FLAG, Constants.SERVICE_UPDATE_FAIL_MSG);
    }

    /**
     * 查询
     *
     * @param userLoginReqDTO
     * @param logicDO
     * @return
     * @throws Exception
     */
    @Override
    public List<UserLoginResDTO> select(UserLoginReqDTO userLoginReqDTO, LogicDO logicDO) throws Exception {
        return BeanMapperUtil.objConvert(userLoginManage.select(BeanMapperUtil.objConvert(userLoginReqDTO,UserLoginBO.class),logicDO),UserLoginResDTO.class);
    }

    /**
     * 查询所有
     *
     * @param logicDO
     * @return
     * @throws Exception
     */
    @Override
    public List<UserLoginResDTO> selectAll(LogicDO logicDO) throws Exception {
        return BeanMapperUtil.objConvert(userLoginManage.selectAll(logicDO),UserLoginResDTO.class);
    }
}
