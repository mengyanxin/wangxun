package com.mengyan.xin.dal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用于记录以某个Key值为索引的数据库行数
 */
@Getter
@Setter
@ToString
public class LogicDO {
    /**
     * 从index行开始
     */
    private Integer index;
    /**
     * 每页显示的条数
     */
    private Integer offset;
    /**
     * 当前页码
     */
    private Integer page;
    /**
     * 总页数
     */
    private Integer pageCount;
    /**
     * 关键字参数
     */
    private String key;
    /**
     * 是否正序排列
     */
    private Boolean orderAsc;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

}