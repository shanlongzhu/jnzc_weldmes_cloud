package com.gw.entities;

import lombok.Data;

import java.util.List;

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
