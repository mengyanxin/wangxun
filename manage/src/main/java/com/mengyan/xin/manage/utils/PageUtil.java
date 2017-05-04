package com.mengyan.xin.manage.utils;

import com.mengyan.xin.dal.model.LogicDO;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页通用方法
 */
public class PageUtil {

    /**
     * 处理分页参数
     * @param sum 信息数目
     * @param pageSize 每页显示数
     * @param page 当前页码
     * @return
     */
    public static LogicDO doPage(Integer sum,Integer pageSize,Integer page){

        LogicDO logicDO = new LogicDO();
        Integer count;//总页数
        Integer pageIndex;//当前页
        //若信息总数为空或不大于0
        if(sum == null || sum <= 0){
            //设置起始位为0开始
            logicDO.setIndex(0);
            //若每页显示数不大于0
            if(pageSize == null || pageSize <= 0){
                //赋予其默认值PAGE_SIZE
                logicDO.setOffset(Constants.PAGE_SIZE);
            }else {
                //否则取输入的显示数
                logicDO.setOffset(pageSize);
            }
            //设置总页数为1
            logicDO.setPageCount(1);
         //信息总数大于0
        }else{
            //若每页显示数不大于0
            if(pageSize == null || pageSize <= 0){
                //用默认值覆盖掉pageSize
                pageSize = Constants.PAGE_SIZE;
            }
            //设置每页显示数（偏移量）
            logicDO.setOffset(pageSize);

            //信息总数取模每页显示数，若为0（即整除）
            if(sum%pageSize==0){
                count=sum/pageSize;
            //非整除
            }else{
                count=sum/pageSize+1;
            }
            //设置返回模型总页数参数
            logicDO.setPageCount(count);

            //若当前页码小于1
            if(page == null || page<1){
                //设置当前显示页码为第一页
                pageIndex = 1;
            //若当前页码大于总页数了
            }else if(page>count){
                //设置当前显示页码为最后页（及总页数的页码）
                pageIndex = count;
            }else{
                //否则是哪页就取那页
                pageIndex = page;
            }

            //设置查询起始位置（如：第一页从零开始）
            logicDO.setIndex((pageIndex-1)*pageSize);
        }

        return logicDO;
    }

    /**
     * 获取最终数据集合
     * @param inList  传入集合
     * @param logicDO  分页模型
     * @return
     */
    public static List obainPageInfo(List inList, LogicDO logicDO) throws Exception{
        List list = new ArrayList<>();
        int start = logicDO.getIndex();
        int offset = logicDO.getOffset();
        for(int i=0;i<offset;i++){
            if(i+start>=inList.size()) {
                break;
            }else{
                list.add(inList.get(i + start));
            }
        }
        return list;
    }
}
