package com.gw.entities;

import lombok.Data;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/7/7 12:34
 * @Description  用于接收角色的菜单权限信息
 */
@Data
public class ManageRoleMenuInfo {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单权限id列表
     */
    private List<Long> menuIds;
}
