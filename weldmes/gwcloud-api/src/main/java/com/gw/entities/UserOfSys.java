package com.gw.entities;

import lombok.Data;


/**
 * @Author zhanghan
 * @Date 2021/6/4 14:02
 * @Description 系统用户表  此实体类与SysUser 均与sys_user表中字段对应
 * 因写用户登录验证中  SysUser类中属性类型不适用  故建此类
 */
@Data
public class UserOfSys {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态  0：禁用   1：正常'
     */
    private int status;

    /**
     * 部门id
     */
    private Long deptId;

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
    private String last_update_by;

    /**
     * 更新时间
     */
    private String lastUpdateTime;

    /**
     * 是否删除  -1：已删除  0：正常
     */
    private int delFlag;
}
