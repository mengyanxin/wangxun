package com.mengyan.xin.manage.impl.user;

import com.mengyan.xin.dal.mapper.user.UserLoginDOMapper;
import com.mengyan.xin.dal.model.LogicDO;
import com.mengyan.xin.dal.model.user.UserLoginDO;
import com.mengyan.xin.manage.base.user.UserLoginManage;
import com.mengyan.xin.manage.model.user.UserLoginBO;
import com.mengyan.xin.manage.utils.BeanMapperUtil;
import com.mengyan.xin.manage.utils.Constants;
import com.mengyan.xin.manage.utils.ManageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mengyanxin on 2017/5/3.
 */
@Service
public class UserLoginManageImpl implements UserLoginManage{

    @Autowired
    private UserLoginDOMapper userLoginDOMapper;

    /**
     * 新增
     *
     * @param userLoginBO
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public int insert(UserLoginBO userLoginBO) throws Exception {
        if(userLoginBO != null){
            return userLoginDOMapper.insert(BeanMapperUtil.objConvert(userLoginBO, UserLoginDO.class));
        }
        throw new ManageException(Constants.MANAGE_INSERT_FAIL_FLAG,Constants.MANAGE_INSERT_FAIL_MSG);
    }

    /**
     * 删除
     *
     * @param userLoginBO
     * @return
     * @throws Exception
     */
    @Override
    public int delete(UserLoginBO userLoginBO) throws Exception {
        List<UserLoginBO> list = select(userLoginBO,new LogicDO());
        if(list !=null && list.size()>0){
            return userLoginDOMapper.delete(BeanMapperUtil.objConvert(userLoginBO, UserLoginDO.class));
        }
        throw new ManageException(Constants.MANAGE_DELETE_FAIL_FLAG,Constants.MANAGE_DELETE_FAIL_MSG);
    }

    /**
     * 更新
     *
     * @param userLoginBO
     * @param logicDO
     * @return
     * @throws Exception
     */
    @Override
    public int update(UserLoginBO userLoginBO, LogicDO logicDO) throws Exception {
        List<UserLoginBO> list = select(userLoginBO,new LogicDO());
        if(list !=null && list.size()>0){
            return userLoginDOMapper.updateByKey(BeanMapperUtil.objConvert(userLoginBO, UserLoginDO.class),logicDO);
        }
        throw new ManageException(Constants.MANAGE_DELETE_FAIL_FLAG,Constants.MANAGE_DELETE_FAIL_MSG);
    }

    /**
     * 根据条件查询
     *
     * @param userLoginBO
     * @param logicDO
     * @return
     * @throws Exception
     */
    @Override
    public List<UserLoginBO> select(UserLoginBO userLoginBO, LogicDO logicDO) throws Exception {
        return BeanMapperUtil.objConvert(userLoginDOMapper.select(BeanMapperUtil.objConvert(userLoginBO,UserLoginDO.class),logicDO),UserLoginBO.class);
    }

    /**
     * 查询所有
     *
     * @param logicDO
     * @return
     * @throws Exception
     */
    @Override
    public List<UserLoginBO> selectAll(LogicDO logicDO) throws Exception {
        return BeanMapperUtil.objConvert(userLoginDOMapper.selectAll(),UserLoginBO.class);
    }
}
