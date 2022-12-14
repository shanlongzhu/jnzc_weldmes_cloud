// 生产设备管理

import request from "@/utils/request";
import qs from "qs";

// *********采集模块**************

// 获取列表
export function getEquList(params = {}) {
  return request({
    url: "/collection",
    method: "get",
    params
  });
}

// 获取详情
export function getEquDetail(id) {
  return request({
    url: `/collection/${id}`,
    method: "get"
  });
}

// 新增采集模块
export function addEqu(data = {}) {
  return request({
    url: "/collection",
    method: "post",
    data
  });
}

// 修改采集模块
export function editEqu(data = {}) {
  return request({
    url: "/collection",
    method: "put",
    data
  });
}

// 删除采集模块
export function delEqu(params = {}) {
  return request({
    url: "/collection",
    method: "delete",
    params
  });
}

// 采集编号重复后覆盖采集模块
export function coverEqu(data = {}) {
  return request({
    url: "/collection/judgeAfterAddGatherInfo",
    method: "post",
    data
  });
}

// 导出
export function exportExcel(params = {}) {
  return `${process.env.VUE_APP_BASE_API}/collection/excel?${qs.stringify(
    params
  )}`;
}

// ********生产设备***********

// 获取焊机设备列表
export function getWelderList(params = {}) {
  return request({
    url: "/welder",
    method: "get",
    params
  });
}


// 删除焊机设备
export function delWelder(params = {}) {
  return request({
    url: "/welder",
    method: "delete",
    params
  });
}

// 获取全部采集序号
export function getAllGatherNos() {
  return request({
    url: `/collection/getGatherNos`,
    method: "get"
  });
}

// 获取焊机设备详情
export function getWelderDetail(id) {
  return request({
    url: `/welder/${id}`,
    method: "get"
  });
}

// 新增焊机设备
export function addWelder(data = {}) {
  return request({
    url: "/welder",
    method: "post",
    data
  });
}

// 修改焊机设备
export function editWelder(data = {}) {
  return request({
    url: "/welder",
    method: "put",
    data
  });
}

// 修改焊机设备
export function getWeldingModel(params = {}) {
  return request({
    url: "/library/getMachineInfoByFirm",
    method: "get",
    params
  });
}

//根据区域id获取区间
export function findByIdArea(params = {}) {
  return request({
    url: "/library/getBayInfoByArea",
    method: "get",
    params
  });
}

//跨间绑定保存
export function saveAreaBind(data = {}) {
  return request({
    url: "/areaBay/addBayToAreaInfo",
    method: "post",
    data
  });
}

//设备绑定保存
export function saveModelBind(data = {}) {
  return request({
    url: "/firmMachine/addMachineToFirmInfo",
    method: "post",
    data
  });
}

// 获取焊机设备列表无分页
export function getWelderListNoPage(params = {}) {
  return request({
    url: "/welder/welderInfosNoPage",
    method: "get",
    params
  });
}

// 根据焊工id拉取任务列表
export function getTaskInfoByWelderId(params = {}) {
  return request({
    url: "/taskClaim/getTaskInfoByWelderId",
    method: "get",
    params
  });
}

// 根据焊机id查询任务绑定
export function getWeldClaimTaskInfoById(params = {}) {
  return request({
    url: "/taskClaim/getWeldClaimTaskInfoById",
    method: "get",
    params
  });
}

// 焊机任务绑定
export function addTaskClaimInfo(data = {}) {
  return request({
    url: "/taskClaim/addTaskClaimInfo",
    method: "post",
    data
  });
}

// 根据机构id获取设备
export function getModelFindId(params = {}) {
  return request({
    url: "/welder/getWeldInfosByDeptId",
    method: "get",
    params
  });
}

//导出
export function exportWelderExcel(params = {}) {
  return `${process.env.VUE_APP_BASE_API}/welder/excel?${qs.stringify(params)}`;
}



//松下设备
// 获取松下焊机设备列表
export function getSxWelderList(params = {}) {
  return request({
    url: "/sx/getSxProcessIssueInfos",
    method: "get",
    params
  });
}

// 新增焊机设备
export function addSxWelder(data = {}) {
  return request({
    url: "/sx/addSxProcessIssueInfos",
    method: "post",
    data
  });
}

// 获取松下焊机设备详情
export function getSxWelderDetail(params) {
  return request({
    url: `/sx/getSxProcessIssueInfoById`,
    method: "get",
    params
  });
}

// 删除松下焊机设备
export function delSxWelder(params = {}) {
  return request({
    url: "/sx/delSxProcessIssueInfo",
    method: "delete",
    params
  });
}

// 修改松下焊机设备
export function editSxWelder(data = {}) {
  return request({
    url: "/sx/updateSxProcessIssueInfo",
    method: "put",
    data
  });
}


// 获取设备开机 关机 时间
export function getWeldInfoTime(params = {}) {
  return request({
    url: "/welder/getEquipFeatureInfo",
    method: "get",
    params
  });
}



