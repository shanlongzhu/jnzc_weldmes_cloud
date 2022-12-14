package com.gw.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUser implements Serializable {

    /**
     * 角色id
     */
    private Long roleId;

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
     * 部门名称
     */
    private String deptName;

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

    /**
     * 是否删除  -1：已删除  0：正常
     */
    private int delFlag;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 岗位
     */
    private String position;

    /**
     * 角色名
     */
    private String roleName;
}
