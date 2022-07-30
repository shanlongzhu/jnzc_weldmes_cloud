package com.gw.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.gw.entities.TaskInfo;
import com.gw.process.dispatch.service.DispatchService;

import java.util.ArrayList;
import java.util.List;

public class TaskExcelListener extends AnalysisEventListener<TaskInfo> {

    //不使用Spring的依赖注入
    DispatchService dispatchService;

    List<TaskInfo> list = new ArrayList<>();

    @Override
    public void invoke(TaskInfo data, AnalysisContext analysisContext) {

        //将每条信息进行码值转换
        TaskInfo taskInfo = dispatchService.importExcel(data);

        //存储到列表
        list.add(taskInfo);


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        //进行批量入库
        dispatchService.addTaskInfos(list);

        //入库完成 清理 list
        list.clear();

    }


    public TaskExcelListener(DispatchService dispatchService) {

        this.dispatchService = dispatchService;
    }
}
