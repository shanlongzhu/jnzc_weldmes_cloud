
import { Loading } from 'element-ui';

let loadingObj

export function showLoading(){
    loadingObj = Loading.service({
        lock: true,
        text: '加载中',
        spinner: 'el-icon-loading',
        background: 'rgba(255, 255, 255, 0.9)'
      });
}

export function hideLoading(){
  loadingObj.close()
}