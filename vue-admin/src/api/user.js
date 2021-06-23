import request from '@/utils/request'
import qs from 'qs'

export function login(data) {
  return request({
    url: '/sysUser/login',
    method: 'post',
    data: qs.stringify(data)
  })
}

export function getInfo() {
  return request({
    url: '/vue/getInfo',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/sysUser/loginOut',
    method: 'post'
  })
}

export function getTaskInfos(pn) {
  return request({
    url: '/task/list',
    method: 'get'
  })
}

export function queryCurrentPageTaskListController(data) {
  return request({
    url: `/task/listT/${data}`,
    method: 'get'
  })
}
