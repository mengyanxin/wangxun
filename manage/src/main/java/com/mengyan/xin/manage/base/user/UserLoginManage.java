package com.mengyan.xin.manage.base.user;

import com.mengyan.xin.dal.model.LogicDO;
import com.mengyan.xin.manage.model.user.UserLoginBO;

import java.util.List;

/**
 * Created by mengyanxin on 2017/5/3.
 */
public interface UserLoginManage {
    /**
     * 新增
     * @param userLoginBO
     * @return
     * @throws Exception
     */
    int insert(UserLoginBO userLoginBO) throws Exception;

    /**
     * 删除
     * @param userLoginBO
     * @return
     * @throws Exception
     */
    int delete(UserLoginBO userLoginBO) throws Exception;

    /**
     * 更新
     * @param userLoginBO
     * @param logicDO
     * @return
     * @throws Exception
     */
    int update(UserLoginBO userLoginBO, LogicDO logicDO) throws Exception;

    /**
     * 根据条件查询
     * @param userLoginBO
     * @return
     * @throws Exception
     */
    List<UserLoginBO> select(UserLoginBO userLoginBO, LogicDO logicDO) throws Exception;

    /**
     * 查询所有
     * @return
     * @throws Exception
     */
    List<UserLoginBO> selectAll(LogicDO logicDO) throws Exception;

}
