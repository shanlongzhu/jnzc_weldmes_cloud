//生产数据详情分析
import request from "@/utils/request";
import qs from "qs";

//****历史曲线**** */
//获取任务编号列表
export function getTaskArr() {
    return request({
      url: "/task/historyTaskInfos",
      method: "get"
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