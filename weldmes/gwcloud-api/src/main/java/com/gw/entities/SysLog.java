package com.gw.entities;

import lombok.Data;



/**
 * @Author zhanghan
 * @Date 2021/7/14 10:12
 * @Description  日志信息实体类
 * @Params
 */
@Data
public class SysLog {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户操作
     */
    private String operation;

    /**
     * 菜单模块
     */
    private String menuModel;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 执行时长(毫秒)
     */
    private String time;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新人
     */
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    private String lastUpdateTime;

}
