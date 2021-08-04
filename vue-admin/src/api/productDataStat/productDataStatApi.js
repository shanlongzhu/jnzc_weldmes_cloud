//生产数据统计
import request from "@/utils/request";
import qs from "qs";

//*****班组生产数据统计****** */
//获取班组统计列表
export function getTeamDataList(params = {}) {
  return request({
    url: "/team",
    method: "get",
    params
  });
}

//导出班组统计列表
export function exportTeamDataList(params = {}) {
  return `${process.env.VUE_APP_BASE_API}/team/excel?${qs.stringify(params)}`;
}


// ******设备生产数据统计*******/
//获取设备统计列表
export function getEquProDataList(params = {}) {
  return request({
    url: "/device",
    method: "get",
    params
  });
}

//导出设备统计列表
export function exportEquProDataList(params = {}) {
  return `${process.env.VUE_APP_BASE_API}/device/excel?${qs.stringify(params)}`;
}


// ******人员生产数据统计*******/
//获取人员统计列表
export function getPerProDataList(params = {}) {
  return request({
    url: "/person",
    method: "get",
    params
  });
}

//导出人员统计列表
export function exportPerProDataList(params = {}) {
  return `${process.env.VUE_APP_BASE_API}/person/excel?${qs.stringify(params)}`;
}


// ******工件生产数据统计*******/
//获取工件统计列表
export function getWorkProDataList(params = {}) {
  return request({
    url: "/artifact",
    method: "get",
    params
  });
}

//导出工件统计列表
export function exportWorkProDataList(params = {}) {
  return `${process.env.VUE_APP_BASE_API}/artifact/excel?${qs.stringify(params)}`;
}

// ******生产任务数据统计*******/
//获取生产任务列表
export function getTaskDataList(params = {}) {
  return request({
    url: "/ProductionTask",
    method: "get",
    params
  });
}

//导出工件统计列表
export function exportTaskDataList(params = {}) {
  return `${process.env.VUE_APP_BASE_API}/ProductionTask/excel?${qs.stringify(params)}`;
}

