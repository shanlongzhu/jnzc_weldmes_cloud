package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/7/7 18:44
 * @Description
 */
@Data
public class UpdateUserInfo {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 用户信息
     */
    private SysUser sysUser;
}
