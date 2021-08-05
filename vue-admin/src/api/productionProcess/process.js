//************生产过程*************** */
import request from '@/utils/request'
import qs from 'qs'

//************任务工单*************** */

// 获取焊工列表部分页
export function getWelderPeopleListNoPage(params = {}) {
  return request({
    url: "/solderer/noPage",
    method: "get",
    params
  });
}

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
    method: 'put',
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
    method: 'delete',
    data
  })
}

// 批量删除任务
export function delCheckAllProcess(data = {}) {
  return request({
    url: '/task/deleteTaskInfoList',
    method: 'delete',
    data
  })
}

// 批量完成
export function changeAllStatus(data = {}) {
  return request({
    url: '/task/finishTaskInfoList',
    method: 'put',
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
export function setEvaluate(data = {}) {
  return request({
    url: '/task/addComment',
    method: 'post',
    data
  })
}


//获取数据字典
export function getDictionaries(data = {}) {
  return request({
    url: '/sysDictionary/getInfoByType',
    method: 'post',
    data
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


//************焊工管理*************** */

// 获取焊工列表
export function getWelderPeopleList(params = {}) {
  return request({
    url: "/solderer",
    method: "get",
    params
  });
}


// 新增焊工
export function addWelderPeople(data = {}) {
  return request({
    url: "/solderer",
    method: "post",
    data
  });
}

// 修改焊工
export function editWelderPeople(data = {}) {
  return request({
    url: "/solderer",
    method: "put",
    data
  });
}

//删除焊工
export function delWelderPeople(params={}) {
  return request({
    url: `/solderer`,
    method: "delete",
    params
  });
}

// 获取焊工详情
export function getWelderPeopleDetail(id) {
  return request({
    url: `/solderer/${id}`,
    method: "get",
  });
}


// 覆盖焊工
export function coverWelderPeople(data = {}) {
  return request({
    url: "/solderer/judgeAfterAddWelderInfo",
    method: "post",
    data
  });
}


// 导出
export function exportWelderPeopleExcel(params = {}) {
  return `${process.env.VUE_APP_BASE_API}/solderer/excel?${qs.stringify(params)}`
}



//************工艺管理*************** */

//获取工艺库
export function getProcesLibraryList(params = {}) {
  return request({
    url: "/library",
    method: "get",
    params
  });
}

//获取工艺库明细
export function getProcesLibraryDetail(id) {
  return request({
    url: `/library/${id}`,
    method: "get"
  });
}

//删除工艺库
export function delProcesLibrary(params={}) {
  return request({
    url: `/library`,
    method: "delete",
    params
  });
}


//新增工艺库
export function addProcesLibrary(data={}) {
  return request({
    url: `/library`,
    method: "post",
    data
  });
}


//修改工艺库
export function editProcesLibrary(data={}) {
  return request({
    url: `/library`,
    method: "put",
    data
  });
}

//根据工艺库id获取工艺数据
export function getProcesLibraryChild(params) {
  return request({
    url: `/craft`,
    method: "get",
    params
  });
}

//获取工艺数据详情
export function getProcesLibraryChildDetail(id) {
  return request({
    url: `/craft/${id}`,
    method: "get"
  });
}

//根据工艺库id获取已使用的通道号
export function getChannNos(params) {
  return request({
    url: `/craft/getChannelNosById`,
    method: "get",
    params
  });
}

//新增工艺
export function addProcesLibraryChild(data) {
  return request({
    url: `/craft`,
    method: "post",
    data
  });
}

//修改工艺
export function editProcesLibraryChild(data) {
  return request({
    url: `/craft`,
    method: "put",
    data
  });
}

//删除工艺
export function delProcesLibraryChild(params) {
  return request({
    url: `/craft`,
    method: "delete",
    params
  });
}