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



//获取松下CO2工艺列表
export function getSxCo2TechList(params){
  return request({
    url: `/sxCO2/getSxCO2ProcessIssueInfos`,
    method: "get",
    params
  });
}

//新增松下CO2工艺
export function addSxCo2Tech(data){
  return request({
    url: `/sxCO2/addSxCO2ProcessIssueInfo`,
    method: "post",
    data
  });
}


//获取松下CO2工艺详情
export function getSxCo2TechDetail(params){
  return request({
    url: `/sxCO2/getSxCO2ProcessIssueInfoById`,
    method: "get",
    params
  });
}

//修改松下CO2工艺详情
export function editSxCo2TechDetail(data){
  return request({
    url: `/sxCO2/updateSxCO2ProcessIssueInfo`,
    method: "put",
    data
  });
}


//删除松下CO2工艺
export function delSxCo2TechDetail(params){
  return request({
    url: `/sxCO2/deleteSxCO2ProcessIssueInfoById`,
    method: "delete",
    params
  });
}

//获取工艺库下松下CO2通道号已使用
export function getCO2ChannaNoIsUse(params){
  return request({
    url: `/sxCO2/getChannelNosById`,
    method: "get",
    params
  });
}


// *******TIG********/
//获取松下TIG工艺列表
export function getSxTIGTechList(params){
  return request({
    url: `/sxTIG/getSxTIGProcessIssueInfos`,
    method: "get",
    params
  });
}

//新增松下TIG工艺
export function addSxTIGTech(data){
  return request({
    url: `/sxTIG/addSxTIGProcessIssueInfo`,
    method: "post",
    data
  });
}


//获取松下TIG工艺详情
export function getSxTIGTechDetail(params){
  return request({
    url: `/sxTIG/getSxTIGProcessIssueInfoById`,
    method: "get",
    params
  });
}

//修改松下TIG工艺详情
export function editSxTIGTechDetail(data){
  return request({
    url: `/sxTIG/updateSxTIGProcessIssueInfo`,
    method: "put",
    data
  });
}


//删除松下TIG工艺
export function delSxTIGTechDetail(params){
  return request({
    url: `/sxTIG/deleteSxTIGProcessIssueInfo`,
    method: "delete",
    params
  });
}

//获取工艺库下松下TIG通道号已使用
export function getTIGChannaNoIsUse(params){
  return request({
    url: `/sxTIG/getChannelNosById`,
    method: "get",
    params
  });
}



//**********AT3工艺************** */
//获取松下AT3工艺列表
export function getSxAT3TechList(params){
  return request({
    url: `/sxAT3/getSxAT3ProcessIssueInfos`,
    method: "get",
    params
  });
}

//新增松下AT3工艺
export function addSxAT3Tech(data){
  return request({
    url: `/sxAT3/addSxAT3ProcessIssueInfos`,
    method: "post",
    data
  });
}

//获取松下AT3工艺详情
export function getSxAT3TechDetail(params){
  return request({
    url: `/sxAT3/getSxAT3ProcessIssueInfoById`,
    method: "get",
    params
  });
}

//修改松下AT3工艺详情
export function editSxAT3TechDetail(data){
  return request({
    url: `/sxAT3/updateSxAT3ProcessIssueInfo`,
    method: "put",
    data
  });
}

//删除松下AT3工艺
export function delSxAT3TechDetail(params){
  return request({
    url: `/sxAT3/delSxAT3ProcessIssueInfo`,
    method: "delete",
    params
  });
}

//获取工艺库下松下AT3通道号已使用
export function getAT3ChannaNoIsUse(params){
  return request({
    url: `/sxAT3/getChannelNosById`,
    method: "get",
    params
  });
}


// *********FR2***************/
//获取松下FR2工艺列表
export function getSxFR2TechList(params){
  return request({
    url: `/sx/getSxFR2ProcessIssueInfos`,
    method: "get",
    params
  });
}

//新增松下FR2工艺
export function addSxFR2Tech(data){
  return request({
    url: `/sx/addSxFR2ProcessIssueInfo`,
    method: "post",
    data
  });
}

//获取松下FR2工艺详情
export function getSxFR2TechDetail(params){
  return request({
    url: `/sx/getSxFR2ProcessIssueInfoById`,
    method: "get",
    params
  });
}

//修改松下FR2工艺详情
export function editSxFR2TechDetail(data){
  return request({
    url: `/sx/alterSxFR2ProcessIssueInfo`,
    method: "put",
    data
  });
}

//删除松下FR2工艺
export function delSxFR2TechDetail(params){
  return request({
    url: `/sx/delSxFR2ProcessIssueInfo`,
    method: "delete",
    params
  });
}

//获取工艺库下松下FR2通道号已使用
export function getFR2ChannaNoIsUse(params){
  return request({
    url: `/sxFR2/getChannelNosById`,
    method: "get",
    params
  });
}