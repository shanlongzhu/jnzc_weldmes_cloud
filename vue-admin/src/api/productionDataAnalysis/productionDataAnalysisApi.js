//生产数据详情分析
import request from "@/utils/request";
import qs from "qs";

//****历史曲线**** */
//获取任务编号列表
export function getTaskArr(data = []) {
  return request({
    url: "/task/historyTaskInfos",
    method: "post",
    data
  });
}

//获取焊工列表
export function getWelderArr() {
  return request({
    url: "/solderer/historySoldererInfos",
    method: "get"
  });
}

//获取焊机列表
export function getWeldingArr() {
  return request({
    url: "/welder/historyWelderInfos",
    method: "get"
  });
}

//获取历史曲线列表
export function getHistoryList(params = {}) {
  return request({
    url: "/historicalCurve/getHistoryInfos",
    method: "get",
    params
  });
}

//获取历史曲线数据
export function getHistoryTimeData(params = {}) {
  return request({
    url: "/historicalCurve",
    method: "get",
    params
  });
}

//根据表面获取历史曲线
export function getHistoryRepeat(data = {}) {
  return request({
    url: "/historicalCurve/HistoricalCurveByTableName",
    method: "post",
    data
  });
}

