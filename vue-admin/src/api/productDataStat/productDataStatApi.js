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
    return request({
      url: "/team/excel",
      method: "get",
      params
    });
  }
