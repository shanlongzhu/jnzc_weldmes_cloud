package com.gw.config;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/9/28 11:17
 * @Description 导出excel工具类
 */
public class DownExcel {
    public static void download(HttpServletResponse response, Class t, List list, String sheetName, String title) throws IOException, IllegalAccessException,InstantiationException {
        response.setContentType("application/vnd.ms-excel");// 设置文本内省
        response.setCharacterEncoding("utf-8");// 设置字符编码
        response.setHeader("Content-disposition", "attachment;filename="+title+".xlsx"); // 设置响应头
        EasyExcel.write(response.getOutputStream(), t).sheet(sheetName).doWrite(list); //用io流来写入数据
    }
}
