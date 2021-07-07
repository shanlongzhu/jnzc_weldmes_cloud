//************系统管理*************** */
import request from "@/utils/request";
import qs from "qs";

// ************菜单管理***************/
//获取菜单资源
export function getMenuList() {
  return request({
    url: "/sysMenu/getMenuOrButtonInfo",
    method: "post"
  });
}
//新增菜单资源
export function addMenu(data = {}) {
  return request({
    url: "/sysMenu/addMenuOrButtonInfo",
    method: "post",
    data
  });
}
//修改菜单资源
export function editMenu(data = {}) {
  return request({
    url: "/sysMenu/updateMenuAndButtonInfo",
    method: "post",
    data
  });
}

//*******角色模块**************** */

//根据id查询菜单资源明细
export function getMenuDetail(params = {}) {
  return request({
    url: "/sysMenu/getMenuAndButtonInfoById",
    method: "get",
    params
  });
}

//获取角色列表
export function getRoleList(params = {}) {
  return request({
    url: "/role/list",
    method: "get",
    params
  });
}

//编辑角色
export function editRole(data = {}) {
  return request({
    url: "/role/updateRoleInfo",
    method: "post",
    data
  });
}

//删除角色
export function delRole(params = {}) {
  return request({
    url: "/role/delRoleInfoById",
    method: "get",
    params
  });
}

//新增角色
export function addRole(data = {}) {
  return request({
    url: "/role/addRoleInfo",
    method: "post",
    data
  });
}

//根据角色id获取角色详情
export function getRoleDetail(params = {}) {
  return request({
    url: "/role/getRoleInfoById",
    method: "get",
    params
  });
}

//根据角色Id获取当前角色权限列表
export function getCurrentRoleList(params = {}) {
  return request({
    url: "/sysMenu/getMenuAndButtonInfoByRoleId",
    method: "get",
    params
  });
}

//设置角色权限
export function saveRoleList(data = {}) {
    return request({
      url: "/role/manageRoleMenuAndButtonInfo",
      method: "post",
      data
    });
  }
