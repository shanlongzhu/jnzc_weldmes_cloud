package com.gw.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.gw.entities.TaskInfo;
import com.gw.process.dispatch.service.DispatchService;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/10/9 15:33
 * @Description
 */
public class TaskExcelListener extends AnalysisEventListener<TaskInfo> {

    DispatchService dispatchService;

    List<TaskInfo> list = new ArrayList();

    @Override
    public void invoke(TaskInfo data, AnalysisContext analysisContext) {

        TaskInfo taskInfo = dispatchService.importExcel(data);

        list.add(taskInfo);


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        dispatchService.addTaskInfos(list);

        //存储完成 清理 list
        list.clear();

    }


    public TaskExcelListener(DispatchService dispatchService) {

        this.dispatchService = dispatchService;
    }
}
