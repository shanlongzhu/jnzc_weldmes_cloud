import request from '@/utils/request'
import qs from 'qs'
// 获取列表
export function getProcessList(params = {}) {
  return request({
    url: '/task/list',
    method: 'get',
    params
  })
}

// 修改任务
export function editProcess(data = {}) {
  return request({
    url: '/task/updateTaskInfo',
    method: 'post',
    data
  })
}
// 新增任务
export function addProcess(data = {}) {
  return request({
    url: '/task/addTaskInfo',
    method: 'post',
    data
  })
}

// 获取任务等级
export function getlevel() {
  return request({
    url: '/task/getTaskRateList',
    method: 'post'
  })
}

// 获取任务详情
export function getProcessDetail(params = {}) {
  return request({
    url: '/task/queryTaskInfoById',
    method: 'get',
    params
  })
}

// 删除任务
export function delProcess(data = {}) {
  return request({
    url: '/task/deleteTaskInfoInfo',
    method: 'post',
    data
  })
}

// 批量删除任务
export function delCheckAllProcess(data = {}) {
  return request({
    url: '/task/deleteTaskInfoList',
    method: 'post',
    data
  })
}

// 批量完成
export function changeAllStatus(data = {}) {
  return request({
    url: '/task/finishTaskInfoList',
    method: 'post',
    data
  })
}

// 单个改状态完成
export function changeStatus(data = {}) {
  return request({
    url: '/task/taskStatusChange',
    method: 'post',
    data
  })
}

// 获取班组
export function getTeam() {
  return request({
    url: '/task/conditions',
    method: 'get'
  })
}

// 评价
export function setEvaluate(params = {}) {
  return request({
    url: '/task/addComment',
    method: 'get',
    params
  })
}

// 导出
export function exportExcel(params = {}) {
  return `${process.env.VUE_APP_BASE_API}/task/exportExcel?${qs.stringify(params)}`
}

// 导入
export function importExcel(params = {}) {
  return `${process.env.VUE_APP_BASE_API}/task/importExcel`
}
