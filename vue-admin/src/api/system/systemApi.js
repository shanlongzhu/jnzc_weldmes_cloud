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

//根据角色id获取用户
export function getRoleUserList(params = {}) {
  return request({
    url: "/user/getUserInfosByRoleId",
    method: "get",
    params
  });
}





//*******************用户模块***************************** */
//获取部门tree数据
export function getUserTree() {
  return request({
    url: "/user/gradeInfos",
    method: "get"
  });
}

//根据部门id获取用户列表
export function findByIdUserList(params={}) {
  return request({
    url: "/user/getUserInfosByOpt",
    method: "get",
    params
  });
}

//新增用户
export function addUser(data={}) {
  return request({
    url: "/user/addUserInfo",
    method: "post",
    data
  });
}

//编辑用户
export function editUser(data={}) {
  return request({
    url: "/user/updateUserInfo",
    method: "post",
    data
  });
}

//删除用户
export function delUser(params={}) {
  return request({
    url: "/user/delUserInfo",
    method: "get",
    params
  });
}

//获取用户明细
export function findIdUserInfo(params={}) {
  return request({
    url: "/user/getUserInfosById",
    method: "get",
    params
  });
}


//获取用户待选角色-不分页
export function getRolesListNoPage() {
  return request({
    url: "/role/roleInfos",
    method: "get",
  });
}


//***********组织机构***************** */
//根据当前机构id获取机构子集列表
export function getTreeDeptInfo(params={}) {
  return request({
    url: "/dept/getDeptInfos",
    method: "get",
    params
  });
}

//新增组织机构
export function addDept(data={}) {
  return request({
    url: "/dept/addDeptInfo",
    method: "post",
    data
  });
}

//编辑组织机构
export function editDept(data={}) {
  return request({
    url: "/dept/updateDeptInfo",
    method: "post",
    data
  });
}

//删除组织机构
export function delDept(params={}) {
  return request({
    url: "/dept/delDeptInfo",
    method: "get",
    params
  });
}

//获取机构明细
export function findIdDeptInfo(params={}) {
  return request({
    url: "/dept/getDeptInfoById",
    method: "get",
    params
  });
}



//*********字典管理************* */


//获取字典列表
export function getDicType() {
  return request({
    url: "/dictionary/getDictionaryTypeInfo",
    method: "get"
  });
}


//新增字典
export function addDic(data={}) {
  return request({
    url: "/dictionary/addDictionaryInfo",
    method: "post",
    data
  });
}

//编辑字典
export function editDic(data={}) {
  return request({
    url: "/dictionary/updateDictionaryInfo",
    method: "post",
    data
  });
}


//根据字典Type查询列表
export function findTypeList(params={}) {
  return request({
    url: "/dictionary/getDictionaryInfoByType",
    method: "get",
    params
  });
}

//根据id查询字典明细
export function findDicIdInfo(params={}) {
  return request({
    url: "/dictionary/getDictionaryInfoById",
    method: "get",
    params
  });
}

//删除字典明细
export function delDic(params = {}) {
  return request({
    url: "/dictionary/delDictionaryInfo",
    method: "get",
    params
  });
}


//获取字典列表
export function getDicList(params={}) {
  return request({
    url: "/dictionary/getDictionaryInfos",
    method: "get",
    params
  });
}



