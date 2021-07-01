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

  // 删除焊机设备列表
export function delWelder(params = {}) {
  return request({
    url: "/welder",
    method: "delete",
    params
  });
}

// 获取焊机设备详情
export function getWelderDetail(id) {
  return request({
    url: `/welder/${id}`,
    method: "get"
  });
}

  //导出
  export function exportWelderExcel(params = {}) {
    return `${process.env.VUE_APP_BASE_API}/welder/excel?${qs.stringify(
      params
    )}`;
  }
