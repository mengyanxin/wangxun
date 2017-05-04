package com.mengyan.xin.dal.mapper.user;

import com.mengyan.xin.dal.model.LogicDO;
import com.mengyan.xin.dal.model.user.UserLoginDO;
import java.util.List;

public interface UserLoginDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLoginDO record);

    UserLoginDO selectByPrimaryKey(Integer id);

    List<UserLoginDO> selectAll();

    int updateByPrimaryKey(UserLoginDO record);

    int delete(UserLoginDO record);

    List<UserLoginDO> select(UserLoginDO record, LogicDO logicDO);

    List<UserLoginDO> selectBySearchKeyOr(UserLoginDO record, LogicDO logicDO);

    List<UserLoginDO> selectBySearchKeyAnd(UserLoginDO record, LogicDO logicDO);

    List<UserLoginDO> selectByKey(UserLoginDO record, LogicDO logicDO);

    int updateByKey(UserLoginDO record, LogicDO logicDO);
}