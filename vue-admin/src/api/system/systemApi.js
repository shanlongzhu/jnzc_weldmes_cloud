//************系统管理*************** */
import request from '@/utils/request'
import qs from 'qs'

// ************菜单管理***************/
//获取菜单资源
export function getMenuList(){
    return request({
        url: '/sysMenu/getMenuOrButtonInfo',
        method: 'post'
      })
}
//新增菜单资源
export function addMenu(data={}){
    return request({
        url: '/sysMenu/addMenuOrButtonInfo',
        method: 'post',
        data
      })
}
//修改菜单资源
export function editMenu(data={}){
    return request({
        url: '/sysMenu/updateMenuAndButtonInfo',
        method: 'post',
        data
      })
}

//根据id查询菜单资源明细
export function getMenuDetail(params={}){
    return request({
        url: '/sysMenu/getMenuAndButtonInfoById',
        method: 'get',
        params
      })
}