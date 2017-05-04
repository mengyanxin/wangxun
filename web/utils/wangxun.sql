
create database wangxun;

use wangxun;

create table user_login(
	id int primary key auto_increment comment '自增主键',
    user_id varchar(50) unique comment '用户id',
    user_name varchar(50) unique not null comment '用户名称',
    user_password varchar(512) not null comment '用户密码',
    user_salt varchar(50) not null comment '用户密码加盐',
    user_login_time datetime not null comment '用户最后一次登陆时间',
    user_status int not null comment '用户状态1:正常,2:冻结中'
);
































