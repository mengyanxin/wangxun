package com.mengyan.xin.service.utils;

import com.mengyan.xin.dal.mapper.user.UserLoginDOMapper;
import com.mengyan.xin.manage.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by mengyanxin on 2017/4/12.
 */
@Component("TaskTime")
public class TaskTime {

    @Autowired
    private UserLoginDOMapper userLoginDOMapper;

    @Scheduled(cron = "10 0,30 * * * ?")
    public void taskRun() {
        try {
            System.out.println(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            Date date = DateUtil.zeroDateSS(new Date());

            System.out.println(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void task() {
        try {
            System.out.println(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            Date date = DateUtil.zeroDateSSS(new Date());

            System.out.println(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
