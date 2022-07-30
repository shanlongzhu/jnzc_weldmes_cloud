package com.gw.entities;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserLoginInfo implements Serializable {

    /**
     * 用户id
     */
    private Long id;


    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 角色
     */
    private List<String> roles;

    /**
     * 角色id
     */
    private List<Long> roleIds;

    /**
     * 用户状态
     */
    private int status;

    /**
     * 部门id
     */
    private Long deptId;



}
