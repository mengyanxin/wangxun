package com.mengyan.xin.web.model.response.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by mengyanxin on 2017/5/3.
 */
@Getter
@Setter
@ToString
public class UserLoginResVO {
    private Integer id;

    private String userId;

    private String userName;

    private String userPassword;

    private String userSalt;

    private Date userLoginTime;

    private Integer userStatus;
}
