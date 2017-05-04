package com.mengyan.xin.service.base.user;

import com.mengyan.xin.dal.model.LogicDO;
import com.mengyan.xin.service.model.request.user.UserLoginReqDTO;
import com.mengyan.xin.service.model.response.user.UserLoginResDTO;

import java.util.List;

/**
 * Created by mengyanxin on 2017/5/3.
 */
public interface UserLoginService {

    /**
     * 新增
     * @param userLoginReqDTO
     * @return
     * @throws Exception
     */
    UserLoginResDTO insert(UserLoginReqDTO userLoginReqDTO) throws Exception;

    /**
     * 删除
     * @param userLoginReqDTO
     * @return
     * @throws Exception
     */
    UserLoginResDTO delete(UserLoginReqDTO userLoginReqDTO) throws Exception;

    /**
     * 更新
     * @param userLoginReqDTO
     * @return
     * @throws Exception
     */
    UserLoginResDTO update(UserLoginReqDTO userLoginReqDTO, LogicDO logicDO) throws Exception;

    /**
     * 查询
     * @param userLoginReqDTO
     * @return
     * @throws Exception
     */
    List<UserLoginResDTO> select(UserLoginReqDTO userLoginReqDTO, LogicDO logicDO) throws Exception;

    /**
     * 查询所有
     * @return
     * @throws Exception
     */
    List<UserLoginResDTO> selectAll(LogicDO logicDO) throws Exception;
}
